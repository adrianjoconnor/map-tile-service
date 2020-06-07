package com.adrianoc.maptileservice.dto;

public class ImageInfoDto {
    private String title;
    private Integer width;
    private Integer height;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageInfoDto imageInfo = (ImageInfoDto) o;

        if (title != null ? !title.equals(imageInfo.title) : imageInfo.title != null) return false;
        if (width != null ? !width.equals(imageInfo.width) : imageInfo.width != null) return false;
        return height != null ? height.equals(imageInfo.height) : imageInfo.height == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ImageInfoDto{" +
                "title='" + title + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
