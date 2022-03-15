package com.revo.myboard.group;

import lombok.*;

import javax.persistence.*;

/*
 * Created By Revo
 */

@Entity
@Table(name = "user_group")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    private Authority authority;

}
