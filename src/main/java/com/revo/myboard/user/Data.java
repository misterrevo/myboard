package com.revo.myboard.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@lombok.Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Data {

    private String description;
    private int age;
    private String city;
    private String page;
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
