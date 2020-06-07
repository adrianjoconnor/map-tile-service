package com.adrianoc.maptileservice.controller;

import com.adrianoc.maptileservice.dto.AvailableImageDto;
import com.adrianoc.maptileservice.dto.ImageInfoDto;
import com.adrianoc.maptileservice.service.ImageTileService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/v1/image")
public class ImageTileController {
    @Resource
    private ImageTileService imageTileService;

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

    @GetMapping(value = "/props/{imageId}", produces = "application/json")
    public ResponseEntity<ImageInfoDto> getImageProperties(@PathVariable Long imageId) {
        return new ResponseEntity<>(imageTileService.getImageProps(imageId), HttpStatus.OK);
    }

    @GetMapping(value = "/availableImages", produces = "application/json")
    public ResponseEntity<List<AvailableImageDto>> getAvailableImages() {
        return new ResponseEntity<>(imageTileService.getAvailableImages(), HttpStatus.OK);
    }
}
