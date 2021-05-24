package com.example.webtest;

import org.springframework.stereotype.Service;

@Service
public class SimpleEventService implements EventService{

    @Override
//    @PerfLogging
    public void createEvent() {
        System.out.println("Create the Event");
    }

    @Override
//    @PerfLogging
    public void publishEvent() {
        System.out.println("Publish the Event");
    }

    @Override
    public void deleteEvent() {
        System.out.println("Delete the Event");
    }
}
