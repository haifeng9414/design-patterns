package com.dhf;

import com.dhf.builder.Nationality;
import com.dhf.builder.Person;
import com.dhf.builder.SkinColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        Person personWang = new Person.Builder()
                .name("小王")
                .age(25)
                .nationality(Nationality.CHINA)
                .skinColor(SkinColor.YELLOW)
                .build();

        Person personZhang = new Person.Builder()
                .name("小张")
                .age(28)
                .nationality(Nationality.USA)
                .skinColor(SkinColor.WHITE)
                .build();

        Person personLiu = new Person.Builder()
                .name("老王")
                .age(48)
                .nationality(Nationality.JAPAN)
                .skinColor(SkinColor.YELLOW)
                .build();

        LOGGER.info(personWang.toString());
        LOGGER.info(personZhang.toString());
        LOGGER.info(personLiu.toString());
    }
}
