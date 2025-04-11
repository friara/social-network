package com.example.social_network01.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class File {
    public enum FileType {
        DOCUMENT,       // Документы (PDF, DOCX)
        SPREADSHEET,    // Таблицы (XLSX, CSV)
        PRESENTATION,   // Презентации (PPTX)
        IMAGE,          // Изображения
        ARCHIVE,        // Архивы (ZIP, RAR)
        OTHER;           // Прочие типы

        public static FileType fromMimeType(String mimeType) {
            if (mimeType == null) return OTHER;

            return switch (mimeType.split("/")[1]) {
                case "pdf", "msword", "vnd.openxmlformats-officedocument.wordprocessingml.document" -> DOCUMENT;
                case "vnd.ms-excel", "vnd.openxmlformats-officedocument.spreadsheetml.sheet", "csv" -> SPREADSHEET;
                case "vnd.ms-powerpoint", "vnd.openxmlformats-officedocument.presentationml.presentation" -> PRESENTATION;
                case "zip", "x-rar-compressed" -> ARCHIVE;
                case "jpeg", "png", "gif" -> IMAGE;
                default -> OTHER;
            };
        }
        }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String fileName;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FileType fileType;

    @Column(nullable = false, length = 100)
    private String mimeType;

    @Column(nullable = false)
    private Long fileSize;

    @PastOrPresent
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedWhen;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    @JsonBackReference
    private User user;

//    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonBackReference
//    private List<MessageFile> messageFiles = new ArrayList<>();

    // Связь многие-к-одному с Message
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    @JsonBackReference // Игнорируется при сериализации
    private Message message;

    @PrePersist
    protected void onCreate() {
        uploadedWhen = LocalDateTime.now();
    }

    // Получение расширения файла
    public String getFileExtension() {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    // Валидация при сохранении

    @PreUpdate
    private void validateFile() {
        if (fileSize <= 0) {
            throw new IllegalArgumentException("File size must be positive");
        }
    }

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

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
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

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
