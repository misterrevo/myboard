package com.revo.myboard.image;

import lombok.*;

import javax.persistence.*;

/*
 * Created By Revo
 */

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String type;

}
