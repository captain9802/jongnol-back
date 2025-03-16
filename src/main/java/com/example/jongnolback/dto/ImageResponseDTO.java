package com.example.jongnolback.dto;

public class ImageResponseDTO {
    private String imageUrl;
    public ImageResponseDTO() {}
    public ImageResponseDTO(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

