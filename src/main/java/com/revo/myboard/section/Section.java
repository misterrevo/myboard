package com.revo.myboard.section;

import com.revo.myboard.category.Category;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
    @Builder.Default
    @OneToMany(mappedBy = "section", cascade = CascadeType.REMOVE)
    private List<Category> categories = new ArrayList<>();
}
