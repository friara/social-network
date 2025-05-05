package com.example.social_network01.service.file;

import com.example.social_network01.dto.FileDTO;
import com.example.social_network01.exception.custom.InvalidFileTypeException;
import com.example.social_network01.exception.custom.StorageException;
import com.example.social_network01.model.File;
import com.example.social_network01.model.Message;
import com.example.social_network01.repository.FileRepository;
import org.modelmapper.ModelMapper;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService{

    @Value("${file.storage.location}")
    private String fileStorageLocation;

    private final FileRepository fileRepository;
    private final ModelMapper modelMapper;

    public FileServiceImpl(FileRepository fileRepository, ModelMapper modelMapper) {
        this.fileRepository = fileRepository;
        this.modelMapper = modelMapper;
    }

    private void initModelMapperMappings() {
        modelMapper.typeMap(MultipartFile.class, File.class)
                .addMappings(mapper -> {
                    mapper.skip(File::setId);
                    mapper.skip(File::setUploadedWhen);
                    mapper.map(src -> src.getOriginalFilename(), File::setFileName);
                    mapper.map(src -> src.getSize(), File::setFileSize);
                    mapper.map(src -> src.getContentType(), File::setMimeType);
                });
    }

    @Override
    public List<FileDTO> getAllFiles() {
        return fileRepository.findAll().stream()
                .map(file -> modelMapper.map(file, FileDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public FileDTO getFileById(Long id) {
        return fileRepository.findById(id)
                .map(file -> modelMapper.map(file, FileDTO.class))
                .orElseThrow(() -> new StorageException("File not found with id: " + id));
    }

    @Override
    public List<FileDTO> saveFiles(List<MultipartFile> files, Message message) {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }

        Path storagePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();

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

                        File fileEntity = modelMapper.map(file, File.class);
                        fileEntity.setMessage(message);
                        fileEntity.setFileType(File.FileType.fromMimeType(file.getContentType()));

                        validateFile(fileEntity, file);

                        File savedFile = fileRepository.save(fileEntity);
                        return modelMapper.map(savedFile, FileDTO.class); // Конвертируем в DTO

                    } catch (IOException e) {
                        throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
                    }
                })
                .collect(Collectors.toList());
    }


    private void validateFile(File file, MultipartFile uploadedFile) {
        if (file.getMimeType() == null) {
            throw new InvalidFileTypeException("Unknown MIME type for file: " + uploadedFile.getOriginalFilename());
        }

        if (uploadedFile.isEmpty()) {
            throw new StorageException("Failed to store empty file: " + uploadedFile.getOriginalFilename());
        }

        Set<String> allowedTypes = Set.of(
                "application/pdf", "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "text/csv", "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                "application/zip", "application/x-rar-compressed",
                "image/jpeg", "image/png", "image/gif"
        );

        if (!allowedTypes.contains(file.getMimeType())) {
            throw new InvalidFileTypeException("Unsupported file type: " + file.getMimeType());
        }
    }

    @Override
    public Resource loadFile(String filename) throws IOException {
        Path file = Paths.get(fileStorageLocation).resolve(filename).toAbsolutePath().normalize();
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new StorageException("File not found: " + filename);
        }
    }

    @Override
    public void deleteFiles(List<File> files) {
        files.forEach(file -> {
            try {
                Path filePath = Paths.get(fileStorageLocation).resolve(file.getFileName());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new StorageException("Failed to delete file: " + file.getFileName(), e);
            }
        });
        fileRepository.deleteAll(files);
    }
}