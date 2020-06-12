package com.adrianoc.maptileservice.service;

import com.adrianoc.maptileservice.dto.AddImageDto;
import com.adrianoc.maptileservice.dto.ImageInfoDto;
import com.adrianoc.maptileservice.exception.ImageLoadingException;
import com.adrianoc.maptileservice.exception.InvalidParamatersException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.mock;

class AdminServiceTest {
    private AdminService adminService;
    private ImageTileService imageTileService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        imageTileService = mock(ImageTileService.class);
        adminService = new AdminServiceImpl(imageTileService);
    }

    @Test
    void addImage_success() throws IOException {
        AddImageDto addImageDto = getSampleAddImageDto();
        String expectedTitle = "Dolphin";
        int expectedWidth = 1081;
        int expectedHeight = 548;
        adminService.addImage(addImageDto);
        ArgumentCaptor<BufferedImage> bICaptor = ArgumentCaptor.forClass(BufferedImage.class);
        ArgumentCaptor<ImageInfoDto> iIDCaptor = ArgumentCaptor.forClass(ImageInfoDto.class);
        Mockito.verify(imageTileService).addImage(bICaptor.capture(), iIDCaptor.capture());
        BufferedImage bufferedImage = bICaptor.getValue();
        Assertions.assertEquals(expectedWidth, bufferedImage.getWidth());
        Assertions.assertEquals(expectedHeight, bufferedImage.getHeight());
        ImageInfoDto imageInfoDto = iIDCaptor.getValue();
        Assertions.assertEquals(expectedWidth, imageInfoDto.getWidth());
        Assertions.assertEquals(expectedHeight, imageInfoDto.getHeight());
        Assertions.assertEquals(expectedTitle, imageInfoDto.getTitle());
    }

    @Test
    void addImage_blankImageData() throws IOException {
        AddImageDto addImageDto = getSampleAddImageDto();
        addImageDto.setImageBytesBase64("");
        Assertions.assertThrows(InvalidParamatersException.class, () -> adminService.addImage(addImageDto));
    }

    @Test
    void addImage_nullImageData() throws IOException {
        AddImageDto addImageDto = getSampleAddImageDto();
        addImageDto.setImageBytesBase64(null);
        Assertions.assertThrows(InvalidParamatersException.class, () -> adminService.addImage(addImageDto));
    }

    @Test
    void addImage_blankTitle() throws IOException {
        AddImageDto addImageDto = getSampleAddImageDto();
        addImageDto.setTitle("");
        Assertions.assertThrows(InvalidParamatersException.class, () -> adminService.addImage(addImageDto));
    }

    @Test
    void addImage_nullTitle() throws IOException {
        AddImageDto addImageDto = getSampleAddImageDto();
        addImageDto.setTitle(null);
        Assertions.assertThrows(InvalidParamatersException.class, () -> adminService.addImage(addImageDto));
    }

    @Test
    void addImage_invalidImageBinData() throws IOException {
        AddImageDto addImageDto = getSampleAddImageDto();
        addImageDto.setImageBytesBase64("bm90aW1n");
        Assertions.assertThrows(ImageLoadingException.class, () -> adminService.addImage(addImageDto));
    }

    @Test
    void addImage_invalidBase64() throws IOException {
        AddImageDto addImageDto = getSampleAddImageDto();
        addImageDto.setImageBytesBase64("notiCFRmg");
        Assertions.assertThrows(InvalidParamatersException.class, () -> adminService.addImage(addImageDto));
    }

    private AddImageDto getSampleAddImageDto() throws IOException {
        InputStream inputStream = AdminServiceTest.class.getResourceAsStream("/sample-add-image-dto.json");
        return objectMapper.readValue(inputStream, AddImageDto.class);
    }
}
