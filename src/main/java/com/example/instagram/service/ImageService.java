package com.example.instagram.service;

import com.example.instagram.dto.enums.ImageType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String getBase64ImageByName(String imageName, ImageType imageType) throws IOException;
    String uploadImage(MultipartFile profilePic, ImageType imageType) throws IOException;
    boolean deleteImage(String imageName, ImageType imageType);
}
