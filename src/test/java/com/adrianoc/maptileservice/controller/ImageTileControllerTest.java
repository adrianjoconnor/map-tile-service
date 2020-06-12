package com.adrianoc.maptileservice.controller;

import com.adrianoc.maptileservice.dao.ImageSourceDao;
import com.adrianoc.maptileservice.dto.AvailableImageDto;
import com.adrianoc.maptileservice.dto.ImageInfoDto;
import com.adrianoc.maptileservice.exception.ImageConversionException;
import com.adrianoc.maptileservice.exception.InvalidParamatersException;
import com.adrianoc.maptileservice.exception.NotFoundException;
import com.adrianoc.maptileservice.processor.ImageTileProcessorTest;
import com.adrianoc.maptileservice.service.ImageTileService;
import com.adrianoc.maptileservice.testconfig.ControllerTestConfiguration;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = ControllerTestConfiguration.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@WebMvcTest(ImageTileController.class)
public class ImageTileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageTileService imageTileService;

    @MockBean
    private ImageSourceDao imageSourceDao;

    @Test
    public void getTile_success() throws Exception {
        long imageId = 44L;
        int startX = 200;
        int startY = 100;
        int xLen = 200;
        int yLen = 200;
        byte[] responseBytes = getMockJpegResponseBytes();
        when(imageTileService.getMapTile(imageId, startX, startY, xLen, yLen)).thenReturn(responseBytes);
        this.mockMvc.perform(get("/v1/image/getTile/" + imageId + "/" + startX + "/" + xLen + "/" + startY + "/" + yLen))
                .andExpect(status().isOk())
                .andExpect(content().bytes(responseBytes));
    }

    @Test
    public void getTile_invalidParams() throws Exception {
        long imageId = 44L;
        int startX = 200;
        int startY = 100;
        int xLen = 200;
        int yLen = 200;
        when(imageTileService.getMapTile(imageId, startX, startY, xLen, yLen)).thenThrow(new InvalidParamatersException(""));
        this.mockMvc.perform(get("/v1/image/getTile/" + imageId + "/" + startX + "/" + xLen + "/" + startY + "/" + yLen))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getTile_notFound() throws Exception {
        long imageId = 44L;
        int startX = 200;
        int startY = 100;
        int xLen = 200;
        int yLen = 200;
        when(imageTileService.getMapTile(imageId, startX, startY, xLen, yLen)).thenThrow(new NotFoundException(""));
        this.mockMvc.perform(get("/v1/image/getTile/" + imageId + "/" + startX + "/" + xLen + "/" + startY + "/" + yLen))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getTile_imageConversionProblem() throws Exception {
        long imageId = 44L;
        int startX = 200;
        int startY = 100;
        int xLen = 200;
        int yLen = 200;
        when(imageTileService.getMapTile(imageId, startX, startY, xLen, yLen)).thenThrow(new ImageConversionException(""));
        this.mockMvc.perform(get("/v1/image/getTile/" + imageId + "/" + startX + "/" + xLen + "/" + startY + "/" + yLen))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void getImageProps_success() throws Exception {
        long imageId = 7L;
        ImageInfoDto mockImageInfoDto = new ImageInfoDto();
        mockImageInfoDto.setWidth(220);
        mockImageInfoDto.setHeight(110);
        mockImageInfoDto.setTitle("Testimg");
        when(imageTileService.getImageProps(imageId)).thenReturn(mockImageInfoDto);
        this.mockMvc.perform(get("/v1/image/props/" + imageId))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"title\":\"Testimg\",\"width\":220,\"height\":110}"));
    }

    @Test
    public void getImageProps_notFound() throws Exception {
        long imageId = 7L;
        when(imageTileService.getImageProps(imageId)).thenThrow(new NotFoundException(""));
        this.mockMvc.perform(get("/v1/image/props/" + imageId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAvailableImages_success() throws Exception {
        AvailableImageDto availableImage = new AvailableImageDto();
        availableImage.setId(33L);
        availableImage.setTitle("Testimage");
        when(imageTileService.getAvailableImages()).thenReturn(Collections.singletonList(availableImage));
        this.mockMvc.perform(get("/v1/image/availableImages"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"title\":\"Testimage\",\"id\":33}]"));
    }

    private byte[] getMockJpegResponseBytes() throws IOException {
        InputStream inputStream = ImageTileProcessorTest.class.getResourceAsStream("/expected-jpeg-bytes.txt");
        return Base64.getDecoder().decode(IOUtils.toString(inputStream));
    }
}
