package com.revo.myboard.comment;

import com.revo.myboard.like.Like;
import com.revo.myboard.post.Post;
import com.revo.myboard.report.Report;
import com.revo.myboard.user.User;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public final class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    private String content;
    @Builder.Default
    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Like> myLikes = new ArrayList<>();
    private LocalDateTime date;
    private LocalDateTime lastEditDate;
    @Builder.Default
    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Report> reports = new ArrayList<>();
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
