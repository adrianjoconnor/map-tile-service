package com.adrianoc.maptileservice.service;

import com.adrianoc.maptileservice.converter.JpegConverter;
import com.adrianoc.maptileservice.dao.ImageSourceDao;
import com.adrianoc.maptileservice.dto.AvailableImageDto;
import com.adrianoc.maptileservice.dto.ImageInfoDto;
import com.adrianoc.maptileservice.exception.ImageConversionException;
import com.adrianoc.maptileservice.exception.InvalidParamatersException;
import com.adrianoc.maptileservice.exception.NotFoundException;
import com.adrianoc.maptileservice.exception.TileOutOfRangeException;
import com.adrianoc.maptileservice.model.ImageSource;
import com.adrianoc.maptileservice.processor.ImageTileProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ImageTileServiceTest {
    private ImageTileService imageTileService;
    private ImageTileProcessor imageTileProcessor;
    private JpegConverter jpegConverter;
    private ImageSourceDao imageSourceDao;

    private final long presentImageId = 28L;
    private final long presentTallImageId = 38L;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() throws IOException {
        imageTileProcessor = Mockito.mock(ImageTileProcessor.class);
        imageSourceDao = Mockito.mock(ImageSourceDao.class);
        ImageSource imageSource = getSampleImageSource();
        ImageSource tallImageSource = getSampleImageTallSource();
        List<ImageSource> imageSources = new ArrayList<>();
        imageSources.add(imageSource);
        imageSources.add(tallImageSource);
        when(imageSourceDao.findAll()).thenReturn(imageSources);
        jpegConverter = Mockito.mock(JpegConverter.class);
        imageTileService = new ImageTileServiceImpl(imageTileProcessor, imageSourceDao, jpegConverter);
    }

    @Test
    void getMapTile_success() throws TileOutOfRangeException, IOException {
        int startX = 100;
        int startY = 100;
        int xLen = 200;
        int yLen = 200;
        BufferedImage mockTile = getMockBufferedImageTile(startX, startY, xLen, yLen);
        when(imageTileProcessor.getTile(any(BufferedImage.class), eq(startX), eq(xLen), eq(startY), eq(yLen)))
                .thenReturn(mockTile);
        when(jpegConverter.convertToJpeg(mockTile)).thenReturn(getMockJpegTile());
        byte[] resultTile = imageTileService.getMapTile(presentImageId, startX, startY, xLen, yLen);
        Assertions.assertArrayEquals(getMockJpegTile(), resultTile);
    }

    @Test
    void getMapTile_missingImageId() {
        Assertions.assertThrows(InvalidParamatersException.class, ()-> imageTileService.getMapTile(null, 0, 0, 100, 100));
    }

    @Test
    void getMapTile_imageNotFound() {
        Assertions.assertThrows(NotFoundException.class, ()-> imageTileService.getMapTile(554l, 0, 0, 100, 100));
    }

    @Test
    void getMapTile_tileOutOfRange() throws IOException, TileOutOfRangeException {
        int startX = 100;
        int startY = 100;
        int xLen = 500;
        int yLen = 500;
        when(imageTileProcessor.getTile(any(BufferedImage.class), eq(startX), eq(xLen), eq(startY), eq(yLen)))
                .thenThrow(new TileOutOfRangeException(""));
        Assertions.assertThrows(InvalidParamatersException.class,() -> imageTileService.getMapTile(presentImageId, startX, startY, xLen, yLen));
    }

    @Test
    void getMapTile_iOException() throws IOException, TileOutOfRangeException {
        int startX = 100;
        int startY = 100;
        int xLen = 200;
        int yLen = 200;
        BufferedImage mockTile = getMockBufferedImageTile(startX, startY, xLen, yLen);
        when(imageTileProcessor.getTile(any(BufferedImage.class), eq(startX), eq(xLen), eq(startY), eq(yLen)))
                .thenReturn(mockTile);
        when(jpegConverter.convertToJpeg(mockTile)).thenThrow(new IOException());
        Assertions.assertThrows(ImageConversionException.class, () ->imageTileService.getMapTile(presentImageId, startX, startY, xLen, yLen));
    }

    @Test
    void getPreview_successFirstTime() throws IOException {
        when(jpegConverter.convertToJpeg(any(Image.class))).thenReturn(getMockJpegPreview());
        int reqMaxSide = 400;
        byte[] preview = imageTileService.getPreview(presentImageId, reqMaxSide);
        Assertions.assertArrayEquals(getMockJpegPreview(), preview);
    }

    @Test
    void getPreview_tallImageSuccess() throws IOException {
        when(jpegConverter.convertToJpeg(any(Image.class))).thenReturn(getMockJpegPreview());
        int reqMaxSide = 200;
        byte[] preview = imageTileService.getPreview(presentTallImageId, reqMaxSide);
        Assertions.assertArrayEquals(getMockJpegPreview(), preview);
    }

    @Test
    void getPreview_biggerThanImage() throws IOException {
        when(jpegConverter.convertToJpeg(any(Image.class))).thenReturn(getMockJpegPreview());
        int reqMaxSide = 600;
        byte[] preview = imageTileService.getPreview(presentTallImageId, reqMaxSide);
        Assertions.assertArrayEquals(getMockJpegPreview(), preview);
    }

    @Test
    void getPreview_successFromCache() throws IOException {
        when(jpegConverter.convertToJpeg(any(Image.class))).thenReturn(getMockJpegPreview());
        int reqMaxSide = 400;
        imageTileService.getPreview(presentImageId, reqMaxSide);
        byte[] preview = imageTileService.getPreview(presentImageId, reqMaxSide);
        Assertions.assertArrayEquals(getMockJpegPreview(), preview);
    }

    @Test
    void getPreview_successDifferingSize() throws IOException {
        when(jpegConverter.convertToJpeg(any(Image.class))).thenReturn(getMockJpegPreview());
        int reqMaxSide = 400;
        imageTileService.getPreview(presentImageId, 300);
        byte[] preview = imageTileService.getPreview(presentImageId, reqMaxSide);
        Assertions.assertArrayEquals(getMockJpegPreview(), preview);
    }

    @Test
    void getPreview_successDifferingSizeFromCache() throws IOException {
        when(jpegConverter.convertToJpeg(any(Image.class))).thenReturn(getMockJpegPreview());
        int reqMaxSide = 400;
        imageTileService.getPreview(presentImageId, reqMaxSide);
        imageTileService.getPreview(presentImageId, 300);
        byte[] preview = imageTileService.getPreview(presentImageId, reqMaxSide);
        Assertions.assertArrayEquals(getMockJpegPreview(), preview);
    }

    @Test
    void getPreview_nullImageId() throws IOException {
        Assertions.assertThrows(InvalidParamatersException.class, () -> imageTileService.getPreview(null, 400));
    }

    @Test
    void getPreview_negativeMaxSide() throws IOException {
        Assertions.assertThrows(InvalidParamatersException.class, () -> imageTileService.getPreview(presentImageId, -5));
    }

    @Test
    void getPreview_imageNotPresent() throws IOException {
        Assertions.assertThrows(NotFoundException.class, () -> imageTileService.getPreview(544L, 400));
    }

    @Test
    void getPreview_conversionIOException() throws IOException {
        when(jpegConverter.convertToJpeg(any(Image.class))).thenThrow(new IOException());
        int reqMaxSide = 200;
        Assertions.assertThrows(ImageConversionException.class, () -> imageTileService.getPreview(presentTallImageId, reqMaxSide));
    }

    @Test
    void addImage_success() throws IOException {
        long createdImageId = 36L;
        BufferedImage bufferedImage = getMockBufferedImageFull();
        String title = "Dolphin";
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        ImageInfoDto imageInfoDto = new ImageInfoDto();
        imageInfoDto.setTitle(title);
        imageInfoDto.setHeight(height);
        imageInfoDto.setWidth(width);

        ImageSource createdImageSource = getSampleImageSource();
        createdImageSource.setId(createdImageId);
        when(imageSourceDao.save(Mockito.any(ImageSource.class))).thenReturn(createdImageSource);

        AvailableImageDto availableImageDto = imageTileService.addImage(bufferedImage, imageInfoDto);
        Assertions.assertEquals(title, availableImageDto.getTitle());
        Assertions.assertEquals(createdImageId, availableImageDto.getId());
    }

    @Test
    void addImageAndRetrieveInfoAfterwards() throws IOException {
        long createdImageId = 36L;
        BufferedImage bufferedImage = getMockBufferedImageFull();
        String title = "Dolphin";
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        ImageInfoDto imageInfoDto = new ImageInfoDto();
        imageInfoDto.setTitle(title);
        imageInfoDto.setHeight(height);
        imageInfoDto.setWidth(width);

        ImageSource createdImageSource = getSampleImageSource();
        createdImageSource.setId(createdImageId);
        when(imageSourceDao.save(Mockito.any(ImageSource.class))).thenReturn(createdImageSource);

        AvailableImageDto availableImageDto = imageTileService.addImage(bufferedImage, imageInfoDto);
        Assertions.assertEquals(title, availableImageDto.getTitle());
        Assertions.assertEquals(createdImageId, availableImageDto.getId());

        ImageInfoDto imageProps = imageTileService.getImageProps(createdImageId);
        Assertions.assertEquals(title, imageProps.getTitle());
        Assertions.assertEquals(height, imageProps.getHeight());
        Assertions.assertEquals(width, imageProps.getWidth());

        List<AvailableImageDto> availableImageDtos = imageTileService.getAvailableImages();
        AvailableImageDto addedAvailableImage = availableImageDtos.get(0);
        Assertions.assertEquals(createdImageId, addedAvailableImage.getId());
        Assertions.assertEquals(title, addedAvailableImage.getTitle());
    }

    @Test
    void getImageProps_success() {
        ImageInfoDto imageInfoDto = imageTileService.getImageProps(28L);
        Assertions.assertEquals("Dolphin", imageInfoDto.getTitle());
        Assertions.assertEquals(1081, imageInfoDto.getWidth());
        Assertions.assertEquals(548, imageInfoDto.getHeight());
    }

    @Test
    void getImageProps_notFound() {
        Assertions.assertThrows(NotFoundException.class, () -> imageTileService.getImageProps(500L));
    }

    @Test
    void getAvailableImagesTest() {
        List<AvailableImageDto> availableImageDtos = imageTileService.getAvailableImages();
        AvailableImageDto setupAvailableImage = availableImageDtos.get(1);
        Assertions.assertEquals("Dolphin", setupAvailableImage.getTitle());
        Assertions.assertEquals(28L, setupAvailableImage.getId());
        AvailableImageDto setupTallAvailableImage = availableImageDtos.get(0);
        Assertions.assertEquals("TallMap", setupTallAvailableImage.getTitle());
        Assertions.assertEquals(38L, setupTallAvailableImage.getId());
    }

    private ImageSource getSampleImageSource() throws IOException {
        return objectMapper.readValue(ImageTileServiceTest.class.getResourceAsStream("/sample-image-source.json"), ImageSource.class);
    }

    private ImageSource getSampleImageTallSource() throws IOException {
        return objectMapper.readValue(ImageTileServiceTest.class.getResourceAsStream("/sample-tall-image-source.json"), ImageSource.class);
    }

    private BufferedImage getMockBufferedImageTile(int startX, int startY, int xLen, int yLen) throws IOException {
        ImageSource imageSource = getSampleImageSource();
        BufferedImage fullImage = ImageIO.read(new ByteArrayInputStream(imageSource.getBitmapBytes()));
        return fullImage.getSubimage(startX, startY, xLen, yLen);
    }

    private BufferedImage getMockBufferedImageFull() throws IOException {
        ImageSource imageSource = getSampleImageSource();
        return ImageIO.read(new ByteArrayInputStream(imageSource.getBitmapBytes()));
    }

    private byte[] getMockJpegTile() throws IOException {
        InputStream jpegB64InputStream = ImageTileServiceTest.class.getResourceAsStream("/sample-jpeg-result-b64.txt");
        String b64String = new BufferedReader(new InputStreamReader(jpegB64InputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        return Base64.getDecoder().decode(b64String);
    }

    private byte[] getMockJpegPreview() throws IOException {
        InputStream jpegB64InputStream = ImageTileServiceTest.class.getResourceAsStream("/sample-jpeg-preview-b64.txt");
        String b64String = new BufferedReader(new InputStreamReader(jpegB64InputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        return Base64.getDecoder().decode(b64String);
    }
}
