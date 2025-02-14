package com.example.social_network01.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private LocalDateTime createdWhen;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String text;
    private Long answerToComm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDateTime getCreatedWhen() {
        return createdWhen;
    }

    public void setCreatedWhen(LocalDateTime createdWhen) {
        this.createdWhen = createdWhen;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTextToComm() {
        return text;
    }

    public void setTextToComm(String textToComm) {
        this.text = textToComm;
    }

    public Long getAnswerToComm() {
        return answerToComm;
    }

    public void setAnswerToComm(Long answerToComm) {
        this.answerToComm = answerToComm;
    }
}
