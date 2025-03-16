package com.example.jongnolback.dto;

public class ImageDTO {
    private String image;

    // 기본 생성자
    public ImageDTO() {}

    // 생성자
    public ImageDTO(String image) {
        this.image = image;
    }

    // Getter, Setter
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
