package com.example.social_network01.service.media;

import com.example.social_network01.exception.custom.InvalidMediaTypeException;
import com.example.social_network01.exception.custom.StorageException;
import com.example.social_network01.model.Media;
import com.example.social_network01.model.Post;
import com.example.social_network01.repository.MediaRepository;
import org.modelmapper.ModelMapper;
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
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MediaService {

    @Value("${media.storage.location}")
    private String mediaStorageLocation;

    private final MediaRepository mediaRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MediaService(MediaRepository mediaRepository, ModelMapper modelMapper) {
        this.mediaRepository = mediaRepository;
        this.modelMapper = modelMapper;
        initModelMapperMappings();
    }

    private void initModelMapperMappings() {
        modelMapper.typeMap(MultipartFile.class, Media.class)
                .addMappings(mapper -> {
                    mapper.skip(Media::setId);
                    mapper.skip(Media::setUploadedWhen);
                    mapper.map(src -> src.getOriginalFilename(), Media::setFileName);
                    mapper.map(src -> src.getSize(), Media::setFileSize);
                    mapper.map(src -> src.getContentType(), Media::setMimeType);
                });
    }

    public List<Media> saveMediaFiles(List<MultipartFile> files, Post post) {
        Path storagePath = Paths.get(mediaStorageLocation).toAbsolutePath().normalize();

        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            throw new StorageException("Could not create storage directory", e);
        }

        return files.stream()
                .map(file -> {
                    try {
                        String filename = generateUniqueFilename(file.getOriginalFilename());
                        Path targetLocation = storagePath.resolve(filename);
                        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                        Media media = modelMapper.map(file, Media.class);
                        media.setPost(post);
                        media.setFileName(filename); // Установка имени файла
                        media.setMediaType(Media.MediaType.fromMimeType(file.getContentType())); // Определение типа

                        validateMedia(media, file);
                        return mediaRepository.save(media);
                    } catch (IOException e) {
                        throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
                    }
                })
                .collect(Collectors.toList());
    }


    private String generateUniqueFilename(String originalName) {
        String uuid = UUID.randomUUID().toString();
        int dotIndex = originalName.lastIndexOf('.');
        return uuid + (dotIndex > 0 ? originalName.substring(dotIndex) : "");
    }

    private void validateMedia(Media media, MultipartFile file) {
        if (media.getMimeType() == null) {
            throw new InvalidMediaTypeException("Unknown MIME type for file: " + file.getOriginalFilename());
        }

        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file: " + file.getOriginalFilename());
        }

        Set<String> allowedTypes = Set.of(
                "image/jpeg", "image/png", "image/gif",
                "video/mp4", "video/quicktime",
                "audio/mpeg", "audio/wav",
                "application/pdf", "text/plain"
        );

        if (!allowedTypes.contains(media.getMimeType())) {
            throw new InvalidMediaTypeException("Unsupported media type: " + media.getMimeType());
        }
    }

    public Resource getMediaFile(String filename) throws IOException {
        Path file = Paths.get(mediaStorageLocation)
                .resolve(filename)
                .toAbsolutePath()
                .normalize();

        Resource resource = new UrlResource(file.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            // Бросаем StorageException вместо RuntimeException
            throw new StorageException("File not found: " + filename);
        }

        return resource;
    }

    public void deleteMedia(List<Media> mediaList) {
        mediaList.forEach(media -> {
            try {
                Path filePath = Paths.get(mediaStorageLocation).resolve(media.getFileName());
                Files.deleteIfExists(filePath); // Удаление файла из хранилища
            } catch (IOException e) {
                throw new StorageException("Failed to delete file: " + media.getFileName(), e);
            }
        });
        mediaRepository.deleteAll(mediaList); // Удаление записей из БД
    }
}

//package com.example.social_network01.service.media;
//
//import com.example.social_network01.exception.custom.InvalidMediaTypeException;
//import com.example.social_network01.exception.custom.StorageException;
//import com.example.social_network01.model.Media;
//import com.example.social_network01.model.Post;
//import com.example.social_network01.repository.MediaRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//public class MediaService {
//
//    @Value("${media.storage.location}")
//    private String mediaStorageLocation;
//
//    @Autowired
//    private MediaRepository mediaRepository;
//
//
////    public MediaService() {
////        // Создаем директорию для хранения медиафайлов, если она не существует
////        try {
////            Files.createDirectories(Paths.get(mediaStorageLocation));
////        } catch (IOException e) {
////            throw new RuntimeException("Could not create media storage directory", e);
////        }
////    }
//
//    /**
//     * Сохраняет список медиафайлов на сервере и в базе данных.
//     */
//    public List<Media> saveMediaFiles(List<MultipartFile> files, Post post) {
//        Path storagePath = Paths.get(mediaStorageLocation).toAbsolutePath().normalize();
//
//        try {
//            Files.createDirectories(storagePath);
//        } catch (IOException e) {
//            throw new StorageException("Could not create storage directory", e);
//        }
//
//        return files.stream()
//                .map(file -> {
//                    try {
//                        // Генерация уникального имени файла
//                        String filename = generateUniqueFilename(file.getOriginalFilename());
//
//                        Path targetLocation = storagePath.resolve(filename);
//                        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//
//                        Media media = new Media();
//                        media.setPost(post);
//                        media.setFileName(file.getOriginalFilename());
//                        media.setFilePath(targetLocation.toString());
//                        media.setFileSize(file.getSize());
//                        media.setMimeType(file.getContentType());
//                        media.setMediaType(Media.MediaType.fromMimeType(file.getContentType()));
//
//                        validateMedia(media, file);
//                        //log.info("Saved media: {} ({} bytes)", filename, file.getSize());
//
//                        return mediaRepository.save(media);
//                    } catch (IOException e) {
//                        throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
//                    }
//                })
//                .collect(Collectors.toList());
//    }
//
//    private String generateUniqueFilename(String originalName) {
//        String uuid = UUID.randomUUID().toString();
//        String extension = "";
//
//        int dotIndex = originalName.lastIndexOf('.');
//        if (dotIndex > 0) {
//            extension = originalName.substring(dotIndex);
//        }
//
//        return uuid + extension;
//    }
//
//    private void validateMedia(Media media, MultipartFile file) {
//        if (media.getMimeType() == null) {
//            throw new InvalidMediaTypeException("Unknown MIME type for file: " + file.getOriginalFilename());
//        }
//
//        if (file.isEmpty()) {
//            throw new StorageException("Failed to store empty file: " + file.getOriginalFilename());
//        }
//
//        Set<String> allowedTypes = Set.of(
//                "image/jpeg", "image/png", "image/gif",
//                "video/mp4", "video/quicktime",
//                "audio/mpeg", "audio/wav",
//                "application/pdf", "text/plain"
//        );
//
//        if (!allowedTypes.contains(media.getMimeType())) {
//            throw new InvalidMediaTypeException("Unsupported media type: " + media.getMimeType());
//        }
//    }
//
//    /**
//     * Получение медиафайла по имени.
//     */
//    public Resource getMediaFile(String filename) throws IOException {
//        Path file = Paths.get(mediaStorageLocation).resolve(filename).toAbsolutePath().normalize();
//        Resource resource = new UrlResource(file.toUri());
//
//        if (resource.exists() || resource.isReadable()) {
//            return resource;
//        } else {
//            throw new RuntimeException("File not found: " + filename);
//        }
//    }
//}
//
