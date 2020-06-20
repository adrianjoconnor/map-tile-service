package com.adrianoc.maptileservice.populator;

import com.adrianoc.maptileservice.dao.ImageSourceDao;
import com.adrianoc.maptileservice.dto.ImageInfoDto;
import com.adrianoc.maptileservice.service.ImageTileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.awt.image.BufferedImage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DbDefaultDataPopulatorTest {
    private DbDefaultDataPopulator dbDefaultDataPopulator;
    private ImageSourceDao imageSourceDao;
    private ImageTileService imageTileService;

    @BeforeEach
    public void setup() {
        imageSourceDao = mock(ImageSourceDao.class);
        imageTileService = mock(ImageTileService.class);
        dbDefaultDataPopulator = new DbDefaultDataPopulator(imageSourceDao, imageTileService);
    }

    @Test
    void populateDefaultData_noImagesPresent() throws Exception {
        int img1Width = 11870;
        int img1Height = 3892;
        int img2Width = 12854;
        int img2Height = 2889;

        when(imageSourceDao.count()).thenReturn(0L);
        dbDefaultDataPopulator.afterPropertiesSet();
        ArgumentCaptor<BufferedImage> bICaptor = ArgumentCaptor.forClass(BufferedImage.class);
        ArgumentCaptor<ImageInfoDto> iIDCaptor = ArgumentCaptor.forClass(ImageInfoDto.class);
        verify(imageTileService, times(2)).addImage(bICaptor.capture(), iIDCaptor.capture());

        BufferedImage bufferedImage1 = bICaptor.getAllValues().get(0);
        ImageInfoDto imageInfoDto1 = iIDCaptor.getAllValues().get(0);
        Assertions.assertEquals(img1Width, bufferedImage1.getWidth());
        Assertions.assertEquals(img1Height, bufferedImage1.getHeight());
        Assertions.assertEquals(img1Width, imageInfoDto1.getWidth());
        Assertions.assertEquals(img1Height, imageInfoDto1.getHeight());
        Assertions.assertEquals("North Kerry", imageInfoDto1.getTitle());

        BufferedImage bufferedImage2 = bICaptor.getAllValues().get(1);
        ImageInfoDto imageInfoDto2 = iIDCaptor.getAllValues().get(1);
        Assertions.assertEquals(img2Width, bufferedImage2.getWidth());
        Assertions.assertEquals(img2Height, bufferedImage2.getHeight());
        Assertions.assertEquals(img2Width, imageInfoDto2.getWidth());
        Assertions.assertEquals(img2Height, imageInfoDto2.getHeight());
        Assertions.assertEquals("Innsbruck (Ampass)", imageInfoDto2.getTitle());
    }

    @Test
    void populateDefaultData_imagesInDb() throws Exception {
        when(imageSourceDao.count()).thenReturn(3L);
        dbDefaultDataPopulator.afterPropertiesSet();
        verify(imageTileService, times(0)).addImage(any(BufferedImage.class), any(ImageInfoDto.class));
    }
}
