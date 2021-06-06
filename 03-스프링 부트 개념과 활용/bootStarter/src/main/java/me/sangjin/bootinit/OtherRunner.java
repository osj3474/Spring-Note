package me.sangjin.bootinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class OtherRunner implements ApplicationRunner {

    @Autowired
    me.sangjin.Other other;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(other);
    }
}
