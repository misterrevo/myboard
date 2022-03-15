package com.revo.myboard.user;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/*
 * Created By Revo
 */

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Data {

    @Type(type = "text")
    private String description;
    private int age;
    private String city;
    private String page;
    @Enumerated(EnumType.STRING)
    private Gender gender;

}
