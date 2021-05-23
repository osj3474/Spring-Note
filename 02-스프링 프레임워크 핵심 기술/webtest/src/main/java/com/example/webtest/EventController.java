package com.example.webtest;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {
    @InitBinder     // 이걸로 바인더를 등록하는 것임.
    public void init(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(Event.class, new EventProperty());
    }

    @GetMapping("/event/{event}")
    public String getEvent(@PathVariable Event event){
        return event.getId().toString();
    }
}
