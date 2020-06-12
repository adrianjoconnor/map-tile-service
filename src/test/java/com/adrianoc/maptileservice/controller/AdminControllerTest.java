package com.adrianoc.maptileservice.controller;

import com.adrianoc.maptileservice.dao.ImageSourceDao;
import com.adrianoc.maptileservice.dto.AddImageDto;
import com.adrianoc.maptileservice.dto.AvailableImageDto;
import com.adrianoc.maptileservice.exception.ImageLoadingException;
import com.adrianoc.maptileservice.exception.InvalidParamatersException;
import com.adrianoc.maptileservice.populator.DbDefaultDataPopulator;
import com.adrianoc.maptileservice.service.AdminService;
import com.adrianoc.maptileservice.testconfig.ControllerTestConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = ControllerTestConfiguration.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@WebMvcTest(AdminController.class)
public class AdminControllerTest {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private ImageSourceDao imageSourceDao;

    @MockBean
    private DbDefaultDataPopulator dbDefaultDataPopulator;

    @WithMockUser(username = "admin", password = "password", roles = "admin")
    @Test
    public void addImage_success() throws Exception {
        AddImageDto addImageDto = getSampleAddImageDto();
        AvailableImageDto mockAvailableImage = new AvailableImageDto();
        mockAvailableImage.setTitle("NewImg");
        mockAvailableImage.setId(55L);
        when(adminService.addImage(any(AddImageDto.class))).thenReturn(mockAvailableImage);
        this.mockMvc.perform(
                post("/v1/admin/addImage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addImageDto)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"title\":\"NewImg\",\"id\":55}"));
        ArgumentCaptor<AddImageDto> addImageBodyCaptor = ArgumentCaptor.forClass(AddImageDto.class);
        verify(adminService).addImage(addImageBodyCaptor.capture());
        AddImageDto addImageBody = addImageBodyCaptor.getValue();
        Assertions.assertEquals(addImageDto.getTitle(), addImageBody.getTitle());
        Assertions.assertEquals(addImageDto.getImageBytesBase64(), addImageBody.getImageBytesBase64());
    }

    @Test
    public void addImage_unauthorized() throws Exception {
        AddImageDto addImageDto = getSampleAddImageDto();
        AvailableImageDto mockAvailableImage = new AvailableImageDto();
        mockAvailableImage.setTitle("NewImg");
        mockAvailableImage.setId(55L);
        when(adminService.addImage(any(AddImageDto.class))).thenReturn(mockAvailableImage);
        this.mockMvc.perform(
                post("/v1/admin/addImage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addImageDto)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "admin", password = "password", roles = "admin")
    @Test
    public void addImage_invalidParams() throws Exception {
        AddImageDto addImageDto = getSampleAddImageDto();
        addImageDto.setTitle("");
        when(adminService.addImage(any(AddImageDto.class))).thenThrow(new InvalidParamatersException(""));
        this.mockMvc.perform(
                post("/v1/admin/addImage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addImageDto)))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "admin", password = "password", roles = "admin")
    @Test
    public void addImage_imageLoadingException() throws Exception {
            AddImageDto addImageDto = getSampleAddImageDto();
            addImageDto.setImageBytesBase64("ftgybhuvbgyNOT");
            when(adminService.addImage(any(AddImageDto.class))).thenThrow(new ImageLoadingException(""));
            this.mockMvc.perform(
                    post("/v1/admin/addImage")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(addImageDto)))
                    .andExpect(status().isInternalServerError());
        }

    private AddImageDto getSampleAddImageDto() throws IOException {
        InputStream inputStream = AdminControllerTest.class.getResourceAsStream("/sample-add-image-dto.json");
        return objectMapper.readValue(inputStream, AddImageDto.class);
    }
}
