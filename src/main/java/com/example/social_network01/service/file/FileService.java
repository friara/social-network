package com.example.social_network01.service.file;

import com.example.social_network01.dto.FileDTO;

import java.util.List;

public interface FileService {
    FileDTO createFile(FileDTO fileDTO);
    List<FileDTO> getAllFiles();
    FileDTO getFileById(Long id);
    void deleteFile(Long id);
}
