package com.adrianoc.maptileservice.service;

import com.adrianoc.maptileservice.dto.AvailableImageDto;
import com.adrianoc.maptileservice.dto.ImageInfoDto;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Service for retrieving image tiles and properties.
 */
public interface ImageTileService {
    /**
     * Retrieve a tile from an image.
     * @param imageId ID of the image.
     * @param startX The X-co-ordinate to start from.
     * @param StartY The Y-co-ordiante to start from.
     * @param xLen The length along the X-Axis. (width)
     * @param yLen The length along the Y-Axis. (height)
     * @return Byte array containing the contents of a JPEG file representing the tile.
     */
    byte[] getMapTile(Long imageId, int startX, int StartY, int xLen, int yLen);

    /**
     * Get a preview of the image with small dimensions.
     * @param imageId The ID of the image.
     * @param reqMaxSide The requested max side length.
     * @return Byte array containing the contents of a JPEG file representing the preview.
     */
    byte[] getPreview(Long imageId, int reqMaxSide);

    /**
     * Add an image and make it available.
     * @param bufferedImage The image to add.
     * @param imageInfo The properties of the image to add.
     * @return AvailableImageDto representing the image.
     * @throws IOException
     */
    AvailableImageDto addImage(BufferedImage bufferedImage, ImageInfoDto imageInfo) throws IOException;

    /**
     * Get the properties of an image.
     * @param id ID of the image.
     * @return ImageInfoDto containg the image's properties.
     */
    ImageInfoDto getImageProps(long id);

    /**
     * Get a list of available images.
     * @return List of available images including their IDs and titles.
     */
    List<AvailableImageDto> getAvailableImages();
}
