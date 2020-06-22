package com.adrianoc.maptileservice.service;

import com.adrianoc.maptileservice.dto.AddImageDto;
import com.adrianoc.maptileservice.dto.AvailableImageDto;

/**
 * Service for administering the available images.
 */
public interface AdminService {
    /**
     * Add an image and make it available.
     * @param addImageDto AddImageDto representing the image.
     * @return AvailableImageDto containing the ID of the newly created image.
     */
    AvailableImageDto addImage(AddImageDto addImageDto);
}
