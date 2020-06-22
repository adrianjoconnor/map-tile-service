package com.adrianoc.maptileservice.controller;

import com.adrianoc.maptileservice.dto.AddImageDto;
import com.adrianoc.maptileservice.dto.AvailableImageDto;
import com.adrianoc.maptileservice.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Controller for administering the available images.
 */
@Controller
@RequestMapping("/v1/admin")
public class AdminController {
    @Resource
    private AdminService adminService;

    /**
     * Add an image by specifying it's title and binary data.
     * @param addImageDto The iamge to add.
     * @return AvailableImageDto containing the ID of the newly created image.
     */
    @PostMapping(value = "/addImage")
    public ResponseEntity<AvailableImageDto> addImage(@RequestBody AddImageDto addImageDto) {
        return new ResponseEntity<>(adminService.addImage(addImageDto), HttpStatus.OK);
    }
}
