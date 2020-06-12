package com.adrianoc.maptileservice.converter;

import com.adrianoc.maptileservice.exception.InvalidConfigValueException;
import com.adrianoc.maptileservice.processor.ImageTileProcessorTest;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

class JpegConverterTest {
    private JpegConverter jpegConverter;
    private int quality = 85;

    @BeforeEach
    void setup() {
        jpegConverter = new JpegConverter(quality);
    }

    @Test
    void jpegQualityOutOfRange() {
        Assertions.assertThrows(InvalidConfigValueException.class, () -> new JpegConverter(222));
        Assertions.assertThrows(InvalidConfigValueException.class, () -> new JpegConverter(-55));
    }

    @Test
    void convertJpeg_success() throws IOException {
        BufferedImage sourceImage = getMockBufferedImage();
        byte[] jpegBytes = jpegConverter.convertToJpeg(sourceImage);
        Assertions.assertArrayEquals(getExpectedJpeg(), jpegBytes);
    }

    private BufferedImage getMockBufferedImage() throws IOException {
        InputStream inputStream = ImageTileProcessorTest.class.getResourceAsStream("/full-source-image.txt");
        byte[] imageBytes = Base64.getDecoder().decode(IOUtils.toString(inputStream));
        return ImageIO.read(new ByteArrayInputStream(imageBytes));
    }

    private byte[] getExpectedJpeg() throws IOException {
        InputStream inputStream = ImageTileProcessorTest.class.getResourceAsStream("/expected-jpeg-bytes.txt");
        return Base64.getDecoder().decode(IOUtils.toString(inputStream));
    }
}
