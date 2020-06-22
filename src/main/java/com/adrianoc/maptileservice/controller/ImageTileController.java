package com.adrianoc.maptileservice.controller;

import com.adrianoc.maptileservice.dto.AvailableImageDto;
import com.adrianoc.maptileservice.dto.ImageInfoDto;
import com.adrianoc.maptileservice.service.ImageTileService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Controller for retrieving sections of images and their properties.
 */
@Controller
@RequestMapping("/v1/image")
public class ImageTileController {
    @Resource
    private ImageTileService imageTileService;

    /**
     * Get a tile from a given image.
     * @param imageId The ID of the iamge.
     * @param xStart The X co-ordinate to start from.
     * @param xLen The length along the X-Axis (width)
     * @param yStart The Y co-ordinate to start from.
     * @param yLen the length along the Y-Axis (Height)
     * @return Byte array containg the contents of the tile in JPEG format.
     */
    @CrossOrigin("*")
    @GetMapping(value = "/getTile/{imageId}/{xStart}/{xLen}/{yStart}/{yLen}", produces = "image/jpeg")
    public ResponseEntity<byte[]> getTile(
            @PathVariable("imageId") Long imageId,
            @PathVariable("xStart") int xStart,
            @PathVariable("xLen") int xLen,
            @PathVariable("yStart") int yStart,
            @PathVariable("yLen") int yLen
    ) {
        byte[] imageBytes = imageTileService.getMapTile(imageId, xStart, yStart, xLen, yLen);
        return new ResponseEntity<>(imageBytes, HttpStatus.OK);
    }

    /**
     * Retrieve a preview image with small dimensions.
     * @param imageId The Id of the image.
     * @param maxSidePx The max width of height of the longest side of the image.
     * @return Byte array containing the bytes of the image in JPEG format.
     */
    @CrossOrigin("*")
    @GetMapping(value = "/preview/{imageId}/{maxSidePx}", produces = "image/jpeg")
    public ResponseEntity<byte[]> getPreview(
            @PathVariable("imageId") Long imageId,
            @PathVariable("maxSidePx") int maxSidePx
    ) {
        byte[] previewBytes = imageTileService.getPreview(imageId, maxSidePx);
        return new ResponseEntity<>(previewBytes, HttpStatus.OK);
    }

    /**
     * Get the properties of an image including it's dimensions.
     * @param imageId The ID fo the image.
     * @return ImageInfoDto containing the the image's properties.
     */
    @CrossOrigin("*")
    @GetMapping(value = "/props/{imageId}", produces = "application/json")
    public ResponseEntity<ImageInfoDto> getImageProperties(@PathVariable Long imageId) {
        return new ResponseEntity<>(imageTileService.getImageProps(imageId), HttpStatus.OK);
    }

    /**
     * Get a list of the IDs and titles of the available images.
     * @return List of AvailableImageDtos specifying the available images.
     */
    @CrossOrigin("*")
    @GetMapping(value = "/availableImages", produces = "application/json")
    public ResponseEntity<List<AvailableImageDto>> getAvailableImages() {
        return new ResponseEntity<>(imageTileService.getAvailableImages(), HttpStatus.OK);
    }
}
