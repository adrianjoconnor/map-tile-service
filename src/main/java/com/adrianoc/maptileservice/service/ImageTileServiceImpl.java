package com.adrianoc.maptileservice.service;

import com.adrianoc.maptileservice.converter.JpegConverter;
import com.adrianoc.maptileservice.dao.ImageSourceDao;
import com.adrianoc.maptileservice.dto.AvailableImageDto;
import com.adrianoc.maptileservice.dto.ImageInfoDto;
import com.adrianoc.maptileservice.exception.*;
import com.adrianoc.maptileservice.model.ImageSource;
import com.adrianoc.maptileservice.processor.ImageTileProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImageTileServiceImpl implements ImageTileService {
    private static final Logger LOG = LoggerFactory.getLogger(ImageTileServiceImpl.class);

    private ImageTileProcessor imageTileProcessor;
    private ImageSourceDao imageSourceDao;
    private JpegConverter jpegConverter;
    private Map<Long,BufferedImage> bufferedImageCache;
    private Map<Long,ImageInfoDto> imageInfoCache;
    private Map<Long, String> availableImages;

    @Autowired
    public ImageTileServiceImpl(
        @Autowired ImageTileProcessor imageTileProcessor,
        @Autowired ImageSourceDao imageSourceDao,
        @Autowired JpegConverter jpegConverter
    ) throws IOException {
        this.imageTileProcessor = imageTileProcessor;
        this.imageSourceDao = imageSourceDao;
        this.jpegConverter = jpegConverter;
        bufferedImageCache = new HashMap<>();
        imageInfoCache = new HashMap<>();
        availableImages = new HashMap<>();
        populateCache();
    }

    @Override
    public byte[] getMapTile(Long imageId, int startX, int startY, int xLen, int yLen) {
        if (imageId == null) {
            throw new InvalidParamatersException("Image ID is missing.");
        }
        if (!bufferedImageCache.containsKey(imageId)) {
            throw new NotFoundException("Image with ID \"" + imageId + "\" not found.");
        }
        BufferedImage sourceImage = bufferedImageCache.get(imageId);
        try {
            BufferedImage tile = imageTileProcessor.getTile(sourceImage, startX, xLen, startY, yLen);
            return jpegConverter.convertToJpeg(tile);
        } catch (TileOutOfRangeException e) {
            throw new InvalidParamatersException("Tile is out of range for this image: " + e.getMessage());
        } catch (IOException e) {
            throw new ImageConversionException(
                    "Error converting tile to JPEG. mapId: "
                            + imageId
                            + ", startX: " + startX
                            + ", startY: " + startY
                            + ", xLen: " + xLen
                            + ", yLen: " + yLen
                            + ", IOException: " + e.toString()
            );
        }
    }

    @Override
    public void addImage(BufferedImage bufferedImage, ImageInfoDto imageInfo, String title) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "bmp", byteArrayOutputStream);
        byte[] bmpData = byteArrayOutputStream.toByteArray();
        ImageSource imageSource = new ImageSource();
        imageSource.setTitle(title);
        imageSource.setBitmapBytes(bmpData);
        ImageSource createdImage = imageSourceDao.save(imageSource);
        Long id = createdImage.getId();
        bufferedImageCache.put(id, bufferedImage);
        imageInfoCache.put(id, imageInfo);
        availableImages.put(id, title);
    }

    @Override
    public ImageInfoDto getImageProps(long id) {
        return imageInfoCache.get(id);
    }

    @Override
    public List<AvailableImageDto> getAvailableImages() {
        List<AvailableImageDto> availableImageDtos = new ArrayList<>();
        for (Map.Entry<Long,String> entry: availableImages.entrySet()) {
            AvailableImageDto availableImageDto = new AvailableImageDto();
            availableImageDto.setId(entry.getKey());
            availableImageDto.setTitle(entry.getValue());
            availableImageDtos.add(availableImageDto);
        }
        return availableImageDtos;
    }

    private void populateCache() throws IOException {
        Iterable<ImageSource> allMapSources = imageSourceDao.findAll();
        for (ImageSource imageSource : allMapSources) {
            Long id = imageSource.getId();
            if (id == null) {
                throw new InvalidStoredImageException("A stored image has no ID.");
            }
            BufferedImage bufferedImage = createBufferedImage(imageSource.getBitmapBytes());
            ImageInfoDto imageInfo = new ImageInfoDto();
            imageInfo.setTitle(imageSource.getTitle());
            imageInfo.setWidth(bufferedImage.getWidth());
            imageInfo.setHeight(bufferedImage.getHeight());
            bufferedImageCache.put(id, bufferedImage);
            imageInfoCache.put(id, imageInfo);
            availableImages.put(id, imageSource.getTitle());
        }
    }

    /**
     * Create BufferedImage from the contents of a bitmap file (Which includes the header)
     * @param bitmapBytes
     * @return
     */
    private BufferedImage createBufferedImage(byte[] bitmapBytes) throws IOException {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bitmapBytes);
        return ImageIO.read(byteInputStream);
    }
}
