package com.example.social_network01.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Schema(hidden = true)
@Entity
@Data
public class Chat {
    public enum ChatType {
        PRIVATE,        // Личный чат (2 участника)
        GROUP;          // Групповой чат
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatType chatType;
    private String chatName;

    @Past
    private LocalDateTime createdWhen;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "chat")
    private List<ChatMember> chatMembers;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages;


}

