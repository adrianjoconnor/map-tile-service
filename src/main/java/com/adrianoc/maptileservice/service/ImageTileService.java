package com.adrianoc.maptileservice.service;

import com.adrianoc.maptileservice.dto.AvailableImageDto;
import com.adrianoc.maptileservice.dto.ImageInfoDto;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public interface ImageTileService {
    byte[] getMapTile(Long imageId, int startX, int StartY, int xlen, int yLen);
    AvailableImageDto addImage(BufferedImage bufferedImage, ImageInfoDto imageInfo) throws IOException;
    ImageInfoDto getImageProps(long id);
    List<AvailableImageDto> getAvailableImages();
}
