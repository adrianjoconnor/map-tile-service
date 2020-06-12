package com.adrianoc.maptileservice.service;

import com.adrianoc.maptileservice.dto.AddImageDto;
import com.adrianoc.maptileservice.dto.AvailableImageDto;
import com.adrianoc.maptileservice.dto.ImageInfoDto;
import com.adrianoc.maptileservice.exception.ImageLoadingException;
import com.adrianoc.maptileservice.exception.InvalidParamatersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class AdminServiceImpl implements AdminService {
    private final ImageTileService imageTileService;

    @Autowired
    public AdminServiceImpl (@Autowired ImageTileService  imageTileService) {
        this.imageTileService = imageTileService;
    }

    @Override
    public AvailableImageDto addImage(AddImageDto addImageDto) {
        if (StringUtils.isEmpty(addImageDto.getImageBytesBase64())) {
            throw new InvalidParamatersException("Image data not present.");
        }
        if (StringUtils.isEmpty(addImageDto.getTitle())) {
            throw new InvalidParamatersException("Image title not present.");
        }
        try {
            byte[] bitmapBytes = Base64.getDecoder().decode(addImageDto.getImageBytesBase64());
            String title = addImageDto.getTitle();
            ImageInfoDto imageInfoDto = new ImageInfoDto();
            imageInfoDto.setTitle(title);

            ByteArrayInputStream bais = new ByteArrayInputStream(bitmapBytes);
            BufferedImage bufferedImage = ImageIO.read(bais);
            if (bufferedImage == null) {
                throw new ImageLoadingException("Image bytes could not successfully be loaded as an image.");
            }
            imageInfoDto.setHeight(bufferedImage.getHeight());
            imageInfoDto.setWidth(bufferedImage.getWidth());

            return imageTileService.addImage(bufferedImage, imageInfoDto);
        } catch (IOException ioe) {
            throw new ImageLoadingException("Error while loading image. " + ioe.getMessage());
        } catch (IllegalArgumentException iae) {
            throw new InvalidParamatersException("Invalid Base64 provided for image data. " + iae.getMessage());
        }
    }
}
