package com.example.webtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {

    @Value("#{1+1}")
    int value;

    @Value("#{'hello'+'world'}")
    String greeting;

    @Value("#{${my.value} == 100}")
    boolean isMyValue100;

    @Value("${my.value}")
    int myValue;

    @Value("#{sample.data}")
    int sampleData;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(value);
        System.out.println(greeting);
        System.out.println(myValue);
        System.out.println(sampleData);
    }
}
