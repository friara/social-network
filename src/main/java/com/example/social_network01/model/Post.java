package com.example.social_network01.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(hidden = true)
@Entity
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Past
    @Column(nullable = false)
    private LocalDateTime createdWhen;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    private String text;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments  = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Like> likes  = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Repost> reposts  = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    @JsonManagedReference
    private List<Media> media  = new ArrayList<>();

    @Transient
    @JsonIgnore
    public int getPopularityScore() {
        return likes.size() + reposts.size() + comments.size();
    }
}

