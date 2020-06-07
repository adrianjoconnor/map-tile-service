package com.adrianoc.maptileservice.processor;

import com.adrianoc.maptileservice.exception.TileOutOfRangeException;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

@Component
public class ImageTileProcessor {

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
