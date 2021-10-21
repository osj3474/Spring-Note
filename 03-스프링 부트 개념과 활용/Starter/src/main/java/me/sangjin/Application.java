package me.sangjin;

import org.apache.catalina.LifecycleException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws LifecycleException {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }
}
