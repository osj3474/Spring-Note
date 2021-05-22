package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
@EnableAsync
public class AppRunner implements ApplicationRunner {
    @Autowired
    ResourceLoader loader;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Resource resource = loader.getResource("classpath:test.txt");
        if(resource.exists()){
            String content = Files.readString(Path.of(resource.getURI()));
            System.out.println(content);
        }
    }
}
