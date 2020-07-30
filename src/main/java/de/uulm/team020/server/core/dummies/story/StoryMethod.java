package de.uulm.team020.server.core.dummies.story;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method outside of the usual story-scope that is used to server the
 * purpose of an easy Story-Usage
 * 
 * @author Florian Sihler
 * @version 1.0, 04/29/2020
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.SOURCE)
public @interface StoryMethod {

}