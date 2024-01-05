package com.example.instagram.service;

import com.example.instagram.dto.enums.ImageType;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${server.storage.path}")
    private String storgePath;

    private static final String POST_PATH = "/posts";
    private static final String PROFILE_PIC_PATH = "/profile-pic";

    @Override
    public String getImageByName(String imageName, ImageType imageType) throws IOException {
        String imagePath = storgePath;
        switch(imageType){
            case POST -> imagePath += POST_PATH;
            case PROFILE_PIC -> imagePath += PROFILE_PIC_PATH;
        }
        File file = new File(imagePath);
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath, imageName));
        String base64Image = new String(Base64.encodeBase64(imageBytes,false), "UTF-8");
        return base64Image;
    }
}
