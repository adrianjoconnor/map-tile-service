package com.adrianoc.maptileservice.populator;

import com.adrianoc.maptileservice.dao.ImageSourceDao;
import com.adrianoc.maptileservice.dto.ImageInfoDto;
import com.adrianoc.maptileservice.service.ImageTileService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Creates default image entries in the database from images in resources.
 */
@Component
public class DbDefaultDataPopulator implements InitializingBean {
    private ImageSourceDao imageSourceDao;
    private ImageTileService imageTileService;

    @Autowired
    public DbDefaultDataPopulator(
            @Autowired ImageSourceDao imageSourceDao,
            @Autowired ImageTileService imageTileService) {
        this.imageSourceDao = imageSourceDao;
        this.imageTileService = imageTileService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (imageSourceDao.count() == 0) {
            createAndSaveImageSource("/innsbruck-ampass-2.png", "Innsbruck (Ampass)");
            createAndSaveImageSource("/north-kerry.png", "North Kerry");
        }
    }

    private void createAndSaveImageSource(String resImage, String title) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(DbDefaultDataPopulator.class.getResourceAsStream(resImage));
        ImageInfoDto imageInfo = new ImageInfoDto();
        imageInfo.setHeight(bufferedImage.getHeight());
        imageInfo.setWidth(bufferedImage.getWidth());
        imageInfo.setTitle(title);
        imageTileService.addImage(bufferedImage, imageInfo);
    }
}
