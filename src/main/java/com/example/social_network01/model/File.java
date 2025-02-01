package com.example.social_network01.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String filePath;
    private String fileType;
    private LocalDateTime uploadedWhen;

    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private User user;

    @OneToMany(mappedBy = "file")
    private List<MessageFile> messageFiles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public LocalDateTime getUploadedWhen() {
        return uploadedWhen;
    }

    public void setUploadedWhen(LocalDateTime uploadedWhen) {
        this.uploadedWhen = uploadedWhen;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<MessageFile> getMessageFiles() {
        return messageFiles;
    }

    public void setMessageFiles(List<MessageFile> messageFiles) {
        this.messageFiles = messageFiles;
    }
}
