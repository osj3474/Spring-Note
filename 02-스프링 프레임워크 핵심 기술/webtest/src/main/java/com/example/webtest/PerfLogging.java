package com.example.webtest;

import java.lang.annotation.*;

@Documented // javadoc을 위해서
@Target(ElementType.METHOD) // 메소드 대상으로 적용한다.
@Retention(RetentionPolicy.CLASS) // 사라지는 타이밍. SOURCE하면 컴파일 후에 Aspect 사라짐. (최소 CLASS, RUNTIME)
public @interface PerfLogging {
}
