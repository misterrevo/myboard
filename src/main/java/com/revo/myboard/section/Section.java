package com.revo.myboard.section;

import com.revo.myboard.category.Category;
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
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "section", cascade = CascadeType.REMOVE)
    private List<Category> categories;

}
