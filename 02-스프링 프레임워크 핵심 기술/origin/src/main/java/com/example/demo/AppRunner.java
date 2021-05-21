package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    MessageSource messageSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Locale.setDefault(new Locale("en", "US"));
        System.out.println(messageSource.getMessage("greeting", new String[]{"Sangjin"}, Locale.KOREA));
        System.out.println(messageSource.getMessage("greeting", new String[]{"Sangjin"}, Locale.getDefault()));
    }
}
