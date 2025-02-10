package com.example.social_network01.service.file;

import com.example.social_network01.dto.FileDTO;
import com.example.social_network01.model.File;
import com.example.social_network01.repository.FileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public FileDTO createFile(FileDTO fileDTO) {
        File file = modelMapper.map(fileDTO, File.class);
        return modelMapper.map(fileRepository.save(file), FileDTO.class);
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
                .orElse(null);
    }

    @Override
    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }
}

