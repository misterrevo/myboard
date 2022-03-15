package com.revo.myboard.comment;

import com.revo.myboard.like.Like;
import com.revo.myboard.post.Post;
import com.revo.myboard.report.Report;
import com.revo.myboard.user.User;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/*
 * Created By Revo
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;
    @Type(type = "text")
    private String content;
    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Like> myLikes;
    private LocalDateTime date;
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastEditDate;
    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Report> reports;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

}
