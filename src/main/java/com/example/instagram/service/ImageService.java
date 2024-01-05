package com.example.instagram.service;

import com.example.instagram.dto.enums.ImageType;

import java.io.IOException;

public interface ImageService {
    String getImageByName(String imageName, ImageType imageType) throws IOException;
}
