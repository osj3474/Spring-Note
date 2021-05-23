//package com.example.webtest;
//
//import org.springframework.validation.Errors;
//import org.springframework.validation.ValidationUtils;
//import org.springframework.validation.Validator;
//
//public class EventValidator implements Validator {
//    @Override
//    public boolean supports(Class<?> aClass) {
//        return Event.class.equals(aClass);
//    }
//
//    @Override
//    public void validate(Object o, Errors errors) {
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "notempty", "Empty filed is not allowed");
//    }
//}
