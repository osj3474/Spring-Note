package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationContextFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ScopeRunner implements ApplicationRunner {

    @Autowired
    ApplicationContext ctx;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Proto by Single");
        System.out.println(ctx.getBean(Single.class).callProto());
        System.out.println(ctx.getBean(Single.class).callProto());
        System.out.println(ctx.getBean(Single.class).callProto());
    }
}
