package com.revo.myboard.category;

import com.revo.myboard.post.Post;
import com.revo.myboard.section.Section;
import lombok.*;

import javax.persistence.*;
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
public final class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Post> posts;
    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

}
