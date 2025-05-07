package com.example.social_network01.service.workspace;

import com.example.social_network01.dto.WorkspaceDTO;
import com.example.social_network01.exception.custom.ResourceNotFoundException;
import com.example.social_network01.model.Workspace;
import com.example.social_network01.repository.WorkspaceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository,
                                ModelMapper modelMapper) {
        this.workspaceRepository = workspaceRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<WorkspaceDTO> getWorkspaces() {
        return workspaceRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public WorkspaceDTO getWorkspaceById(Long id) {
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with id: " + id));
        return convertToDto(workspace);
    }

    @Override
    @Transactional
    public WorkspaceDTO createWorkspace(WorkspaceDTO workspaceDTO) {
        Workspace workspace = convertToEntity(workspaceDTO);
        Workspace savedWorkspace = workspaceRepository.save(workspace);
        return convertToDto(savedWorkspace);
    }

    @Override
    @Transactional
    public void deleteWorkspaceById(Long id) {
        if (!workspaceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Workspace not found with id: " + id);
        }
        workspaceRepository.deleteById(id);
    }

    @Override
    @Transactional
    public WorkspaceDTO updateWorkspace(WorkspaceDTO workspaceDTO) {
        Workspace existingWorkspace = workspaceRepository.findById(workspaceDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with id: " + workspaceDTO.getId()));

        modelMapper.map(workspaceDTO, existingWorkspace);
        Workspace updatedWorkspace = workspaceRepository.save(existingWorkspace);
        return convertToDto(updatedWorkspace);
    }

    @Override
    public List<WorkspaceDTO> getAvailableWorkspaces() {
        return workspaceRepository.findByIsAvailableTrue()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private WorkspaceDTO convertToDto(Workspace workspace) {
        return modelMapper.map(workspace, WorkspaceDTO.class);
    }

    private Workspace convertToEntity(WorkspaceDTO workspaceDTO) {
        return modelMapper.map(workspaceDTO, Workspace.class);
    }
}