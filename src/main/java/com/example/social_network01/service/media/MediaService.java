package com.example.social_network01.service.media;

import com.example.social_network01.model.Media;
import com.example.social_network01.model.Post;
import com.example.social_network01.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MediaService {

    @Value("${media.storage.location}")
    private String mediaStorageLocation;

    @Autowired
    private MediaRepository mediaRepository;


//    public MediaService() {
//        // Создаем директорию для хранения медиафайлов, если она не существует
//        try {
//            Files.createDirectories(Paths.get(mediaStorageLocation));
//        } catch (IOException e) {
//            throw new RuntimeException("Could not create media storage directory", e);
//        }
//    }

    /**
     * Сохраняет список медиафайлов на сервере и в базе данных.
     */
    public List<Media> saveMediaFiles(List<MultipartFile> files, Post post) {
        return files.stream()
                .map(file -> {
                    try {
                        // Сохраняем файл в директории хранения
                        Path targetLocation = Paths.get(mediaStorageLocation)
                                .resolve(file.getOriginalFilename())
                                .toAbsolutePath()
                                .normalize();
                        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                        // Создаем объект Media и сохраняем его в базе данных
                        Media media = new Media();
                        media.setMediaType(file.getContentType());
                        media.setFileName(file.getOriginalFilename());
                        media.setFilePath(targetLocation.toString());
                        media.setUploadedWhen(LocalDateTime.now());
                        media.setPost(post);
                        return mediaRepository.save(media);
                    } catch (IOException e) {
                        throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", e);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Получение медиафайла по имени.
     */
    public Resource getMediaFile(String filename) throws IOException {
        Path file = Paths.get(mediaStorageLocation).resolve(filename).toAbsolutePath().normalize();
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("File not found: " + filename);
        }
    }
}
