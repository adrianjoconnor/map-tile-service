package com.adrianoc.maptileservice.dto;

public class AddImageDto {
    private String title;
    private String imageBytesBase64;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageBytesBase64() {
        return imageBytesBase64;
    }

    public void setImageBytesBase64(String imageBytesBase64) {
        this.imageBytesBase64 = imageBytesBase64;
    }
}
