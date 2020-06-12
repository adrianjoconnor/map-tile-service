package com.adrianoc.maptileservice.dao;

import com.adrianoc.maptileservice.model.ImageSource;
import com.adrianoc.maptileservice.populator.DbDefaultDataPopulator;
import com.adrianoc.maptileservice.testconfig.DaoTestConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@ContextConfiguration(classes = DaoTestConfiguration.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
public class ImageSourceDaoTest {
    @Autowired
    private ImageSourceDao imageSourceDao;

    @MockBean
    private DbDefaultDataPopulator dbDefaultDataPopulator;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void createAndRetrieveTest() throws IOException {
        ImageSource imageSource = getSampleImageSource();
        imageSourceDao.save(imageSource);
        // Will always be first auto-generated ID
        ImageSource retrievedImageSource = imageSourceDao.findById(1L).get();
        Assertions.assertArrayEquals(imageSource.getBitmapBytes(), retrievedImageSource.getBitmapBytes());
        Assertions.assertEquals(1L, retrievedImageSource.getId());
        Assertions.assertEquals(imageSource.getTitle(), retrievedImageSource.getTitle());
    }

    private ImageSource getSampleImageSource() throws IOException {
        return objectMapper.readValue(ImageSourceDaoTest.class.getResourceAsStream("/sample-image-source.json"), ImageSource.class);
    }
}
