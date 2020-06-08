package com.adrianoc.maptileservice.service;

import com.adrianoc.maptileservice.dto.AddImageDto;
import com.adrianoc.maptileservice.dto.AvailableImageDto;

public interface AdminService {
    AvailableImageDto addImage(AddImageDto addImageDto);
}
