package com.revo.myboard.security.annotation;

import org.springframework.security.access.annotation.Secured;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Secured And Permitted For User And Higher Group
 *
 * Created By Revo
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Secured({"ROLE_USER", "ROLE_MODERATOR", "ROLE_ADMIN"})
public @interface ForUser {
}