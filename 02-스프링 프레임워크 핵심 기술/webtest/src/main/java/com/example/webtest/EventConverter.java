package com.example.webtest;

import org.springframework.core.convert.converter.Converter;

public class EventConverter {
    public static class StringToEventConverter implements Converter<String, Event>{
        @Override
        public Event convert(String s) {
            return new Event(Integer.parseInt(s));
        }
    }

    public static class EventToStringConverter implements Converter<Event, String>{
        @Override
        public String convert(Event event) {
            return event.getId().toString();
        }
    }
}
