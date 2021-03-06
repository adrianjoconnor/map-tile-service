package com.adrianoc.maptileservice.converter;

import com.adrianoc.maptileservice.exception.InvalidConfigValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Converter which takes an image in raw form and returns the contents of a JPEG file for that image.
 */
@Component
public class JpegConverter {
    private int jpegQuality;

    @Autowired
    public JpegConverter(@Value("${jpeg.conversion.quality}") int jpegQuality) {
        if (jpegQuality < 0 || jpegQuality > 100) {
            throw new InvalidConfigValueException("JPEG Quality is not within the required bounds.");
        }
        this.jpegQuality = jpegQuality;
    }

    /**
     * Convert an Image to JPEG
     * @param image Image to convert.
     * @return Byte array containing the contents of a JPEG file representing the image.
     * @throws IOException
     */
    public byte[] convertToJpeg(Image image) throws IOException {
        if (image instanceof BufferedImage)
        {
            return convertToJpeg((BufferedImage) image);
        }
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        return convertToJpeg(bufferedImage);
    }

    /**
     * Convert a BufferedImage to Jpeg.
     * @param bufferedImage The image to convert
     * @return Byte array containing the contents of a JPEG file representing the image.
     * @throws IOException
     */
    public byte[] convertToJpeg(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        JPEGImageWriteParam jpegImageWriteParam = new JPEGImageWriteParam(null);
        jpegImageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpegImageWriteParam.setCompressionQuality(0.01f * jpegQuality);

        ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream);
        imageWriter.setOutput(imageOutputStream);
        IIOImage iioImage = new IIOImage(bufferedImage, null, null);
        imageWriter.write(null, iioImage, jpegImageWriteParam);

        return byteArrayOutputStream.toByteArray();
    }
}
