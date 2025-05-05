package com.example.social_network01.service.file;

import com.example.social_network01.dto.FileDTO;
import com.example.social_network01.model.File;
import com.example.social_network01.model.Message;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<FileDTO> getAllFiles();
    FileDTO getFileById(Long id);

    List<FileDTO> saveFiles(List<MultipartFile> files, Message message);

    Resource loadFile(String filename) throws IOException;

    void deleteFiles(List<File> files);

    default String generateUniqueFilename(String originalName) {
        String uuid = java.util.UUID.randomUUID().toString();
        int dotIndex = originalName.lastIndexOf('.');
        return uuid + (dotIndex > 0 ? originalName.substring(dotIndex) : "");
    }
}
