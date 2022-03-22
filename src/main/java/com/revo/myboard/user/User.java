package com.revo.myboard.user;

import com.revo.myboard.comment.Comment;
import com.revo.myboard.group.Group;
import com.revo.myboard.like.Like;
import com.revo.myboard.post.Post;
import com.revo.myboard.report.Report;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@lombok.Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String login;
    private String password;
    @Column(unique = true)
    private String email;
    private boolean blocked;
    private boolean active;
    @Builder.Default
    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "author")
    private List<Comment> comments = new ArrayList<>();
    @Builder.Default
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "reporter")
    private List<Report> reports = new ArrayList<>();
    @Embedded
    private Data data;
    @Builder.Default
    @OneToMany(mappedBy = "who", cascade = CascadeType.REMOVE)
    private List<Like> liked = new ArrayList<>();
    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "group_id")
    private Group group;
    private String code;
    public LocalDateTime lastActivityDate;
}
