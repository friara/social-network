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
public class AvatarService {

    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Value("${avatar.storage-path}")
    private String avatarStorageLocation;

    public String saveImage(MultipartFile file) {
        try {
            Path targetLocation = Paths.get(avatarStorageLocation)
                    .resolve(file.getOriginalFilename())
                    .toAbsolutePath()
                    .normalize();
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", e);
        }
    }

    public Resource getImageFile(String filename) throws IOException {
        Path file = Paths.get(avatarStorageLocation).resolve(filename).toAbsolutePath().normalize();
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("File not found: " + filename);
        }
    }
}
