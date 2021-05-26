package com.example.webtest;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    @NonNull
    public String helloName(@NonNull String name){
        return "Hello, " + name;
    }
}
