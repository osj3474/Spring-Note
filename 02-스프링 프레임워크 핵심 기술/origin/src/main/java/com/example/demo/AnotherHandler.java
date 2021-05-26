package com.example.demo;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AnotherHandler {
    @EventListener
    @Async
    public void handle(MyEvent event){
        System.out.println(Thread.currentThread());
        System.out.println("Another "+ event.getData());
    }
}
