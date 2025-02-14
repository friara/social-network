package com.example.social_network01.service.media;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    @Value("${image.storage.location}")
    private String imageStorageLocation;

//    public ImageService() {
//        // Создаем директорию для хранения изображений, если она не существует
//        try {
//            Files.createDirectories(Paths.get(imageStorageLocation));
//        } catch (IOException e) {
//            throw new RuntimeException("Could not create image storage directory", e);
//        }
//    }

    public String saveImage(MultipartFile file) {
        try {
            Path targetLocation = Paths.get(imageStorageLocation)
                    .resolve(file.getOriginalFilename())
                    .toAbsolutePath()
                    .normalize();
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", e);
        }
    }

    /**
     * Получение изображения по имени.
     */
    public Resource getImageFile(String filename) throws IOException {
        Path file = Paths.get(imageStorageLocation).resolve(filename).toAbsolutePath().normalize();
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("File not found: " + filename);
        }
    }
}
