package com.example.social_network01.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(hidden = true)
@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    private String text;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdWhen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private MessageStatus status = MessageStatus.SENT;  // Значение по умолчанию

    // Связь один-ко-многим с File
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Управление сериализацией
    private List<File> files = new ArrayList<>();

    public enum MessageStatus {
        SENT, DELIVERED, READ, EDITED, DELETED
    }

    @OneToMany(mappedBy = "linkedMessage", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MessageNotification> notifications = new ArrayList<>();


}
