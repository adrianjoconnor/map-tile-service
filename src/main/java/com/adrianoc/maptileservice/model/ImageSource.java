package com.adrianoc.maptileservice.model;

import javax.persistence.*;
import java.util.Arrays;

@Entity
public class ImageSource {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(name = "title")
    private String title;
    @Lob
    @Column(name = "bitmapBytes", columnDefinition="BLOB")
    private byte[] bitmapBytes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getBitmapBytes() {
        return bitmapBytes;
    }

    public void setBitmapBytes(byte[] bitmapBytes) {
        this.bitmapBytes = bitmapBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageSource that = (ImageSource) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return Arrays.equals(bitmapBytes, that.bitmapBytes);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(bitmapBytes);
        return result;
    }

    @Override
    public String toString() {
        return "ImageSource{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", bitmapBytes=" + Arrays.toString(bitmapBytes) +
                '}';
    }
}
