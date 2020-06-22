package com.adrianoc.maptileservice.processor;

import com.adrianoc.maptileservice.exception.TileOutOfRangeException;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

/**
 * For retrieving a tile from an image.
 */
@Component
public class ImageTileProcessor {

    /**
     * Retrieve a tile or cropped version of an image.
     * @param sourceBitmap The source image to crop.
     * @param startX The X-co-ordinate to start from.
     * @param xLen The length along the X-Axis (Width)
     * @param startY The Y-co-ordinate to start from.
     * @param yLen The length along the Y-Axis (Height)
     * @return BufferedImage containing the specified section of the image.
     * @throws TileOutOfRangeException If it is not possible to create this tile from the image.
     */
    public BufferedImage getTile(BufferedImage sourceBitmap, int startX, int xLen, int startY, int yLen) throws TileOutOfRangeException {
        try {
            return sourceBitmap.getSubimage(startX, startY, xLen, yLen);
        } catch (RasterFormatException e) {
            throw new TileOutOfRangeException(
                "Source tile could not be cropped to desired co-ordinates/dimensions. Source bitmap height: "
                    + sourceBitmap.getHeight()
                    + ", source bitmap width: "
                    + sourceBitmap.getWidth()
                    + ", startX: "
                    + startX
                    + ", xLen:"
                    + xLen
                    + ", startY: "
                    + startY
                    + ", yLen:"
                    + yLen
            );
        }
    }

}
