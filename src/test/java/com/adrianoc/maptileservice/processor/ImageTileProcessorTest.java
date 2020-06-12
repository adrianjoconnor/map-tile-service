package com.adrianoc.maptileservice.processor;

import com.adrianoc.maptileservice.exception.TileOutOfRangeException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class ImageTileProcessorTest {
    private ImageTileProcessor imageTileProcessor = new ImageTileProcessor();

    @Test
    void getTile_success() throws IOException, TileOutOfRangeException {
        BufferedImage inputImage = getMockBufferedImage();
        int startX = 120;
        int startY = 120;
        int width = 200;
        int height = 250;
        BufferedImage resultTile = imageTileProcessor.getTile(inputImage, startX, width, startY, height);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(resultTile, "bmp", byteArrayOutputStream);
        byte[] bmpData = byteArrayOutputStream.toByteArray();
        Assertions.assertArrayEquals(getExpectedTile(), bmpData);
        Assertions.assertEquals(width, resultTile.getWidth());
        Assertions.assertEquals(height, resultTile.getHeight());
    }

    @Test
    void getTile_outOfRange() throws IOException {
        BufferedImage inputImage = getMockBufferedImage();
        int startX = 3000;
        int startY = 4000;
        int width = 20504;
        int height = 27332;
        Assertions.assertThrows(TileOutOfRangeException.class, () -> imageTileProcessor.getTile(inputImage, startX, width, startY, height));
    }

    private BufferedImage getMockBufferedImage() throws IOException {
        InputStream inputStream = ImageTileProcessorTest.class.getResourceAsStream("/full-source-image.txt");
        byte[] imageBytes = Base64.getDecoder().decode(IOUtils.toString(inputStream));
        return ImageIO.read(new ByteArrayInputStream(imageBytes));
    }

    private byte[] getExpectedTile() throws IOException {
        InputStream inputStream = ImageTileProcessorTest.class.getResourceAsStream("/expected-tile-data.txt");
        return Base64.getDecoder().decode(IOUtils.toString(inputStream));
    }
}
