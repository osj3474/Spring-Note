package com.example.webtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;

@Component
public class EventRunner implements ApplicationRunner {
    @Autowired
    Validator validator;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // ======== Errors 구현 (Spring MVC에서는 알아서 해줌) ======= //
        Event event = new Event();
        event.setLimit(150);       // 일부러 error상황 유도
        event.setEmail("test");    // 일부러 error상황 유도
        Errors errors = new BeanPropertyBindingResult(event, "event"); // args: (Target Event, Name)

        validator.validate(event, errors); // event의 검증 결과를 errors에 담음
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(e->{
                System.out.println("====== Error Code ======");
                Arrays.stream(e.getCodes()).forEach(System.out::println);
            });
        }
    }
}
