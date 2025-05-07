package com.example.social_network01.controller;

import com.example.social_network01.dto.WorkspaceDTO;
import com.example.social_network01.service.workspace.WorkspaceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @Autowired
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping
    public ResponseEntity<List<WorkspaceDTO>> getAllWorkspaces() {
        List<WorkspaceDTO> workspaces = workspaceService.getWorkspaces();
        return ResponseEntity.ok(workspaces);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceDTO> getWorkspaceById(@PathVariable Long id) {
        WorkspaceDTO workspace = workspaceService.getWorkspaceById(id);
        return ResponseEntity.ok(workspace);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkspaceDTO> createWorkspace(
            @RequestBody @Valid WorkspaceDTO workspaceDTO) {
        WorkspaceDTO createdWorkspace = workspaceService.createWorkspace(workspaceDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdWorkspace.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdWorkspace);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkspaceDTO> updateWorkspace(
            @PathVariable Long id,
            @RequestBody @Valid WorkspaceDTO workspaceDTO) {
        workspaceDTO.setId(id);
        WorkspaceDTO updatedWorkspace = workspaceService.updateWorkspace(workspaceDTO);
        return ResponseEntity.ok(updatedWorkspace);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long id) {
        workspaceService.deleteWorkspaceById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<WorkspaceDTO>> getAvailableWorkspaces() {
        List<WorkspaceDTO> availableWorkspaces = workspaceService.getAvailableWorkspaces();
        return ResponseEntity.ok(availableWorkspaces);
    }
}