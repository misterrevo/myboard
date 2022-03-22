package com.revo.myboard.group;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_group")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    private Authority authority;
}
