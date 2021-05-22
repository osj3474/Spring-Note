package com.example.demo;

import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MyEventHandler {
    @EventListener
    @Async
    public void myHandle(MyEvent event){
        System.out.println(Thread.currentThread());

        System.out.println("이벤트 발생 / 데이터 : " + event.getData());
    }

    @EventListener
    public void handle(ContextRefreshedEvent event){
        System.out.println("ContextRefreshedEvent");
    }

    @EventListener
    public void handle(ContextClosedEvent event){
        System.out.println("ContextRefreshedEvent");
    }


}
