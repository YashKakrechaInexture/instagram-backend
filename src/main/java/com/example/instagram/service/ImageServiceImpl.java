package com.example.instagram.service;

import com.example.instagram.dto.enums.ImageType;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${server.storage.path}")
    private String storagePath;

    private static final String POST_PATH = "/posts";
    private static final String PROFILE_PIC_PATH = "/profile-pic";

    @Override
    public String getBase64ImageByName(String imageName, ImageType imageType) throws IOException {
        String imagePath = storagePath;
        switch(imageType){
            case POST -> imagePath += POST_PATH;
            case PROFILE_PIC -> imagePath += PROFILE_PIC_PATH;
        }
        File file = new File(imagePath);
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath, imageName));
        String base64Image = new String(Base64.encodeBase64(imageBytes,false), "UTF-8");
        return base64Image;
    }

    @Override
    public String uploadImage(MultipartFile profilePic, ImageType imageType) throws IOException {
        String imagePath = storagePath;
        switch(imageType){
            case POST -> imagePath += POST_PATH;
            case PROFILE_PIC -> imagePath += PROFILE_PIC_PATH;
        }
        String imageName = UUID.randomUUID().toString();
        String[] imageNameParts = profilePic.getOriginalFilename().split("\\.");
        String extension = imageNameParts[imageNameParts.length-1];
        imageName += "."+extension;
        Path destinationPath = Paths.get(imagePath, imageName);
        profilePic.transferTo(destinationPath.toFile());
        return imageName;
    }

    @Override
    public boolean deleteImage(String imageName, ImageType imageType) {
        String imagePath = storagePath;
        switch(imageType){
            case POST -> imagePath += POST_PATH;
            case PROFILE_PIC -> imagePath += PROFILE_PIC_PATH;
        }
        File file = new File(imagePath,imageName);
        return file.delete();
    }
}
