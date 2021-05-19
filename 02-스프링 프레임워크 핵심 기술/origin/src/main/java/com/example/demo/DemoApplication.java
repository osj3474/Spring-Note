package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import other.OtherPack;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	OtherPack otherPack;

	public static void main(String[] args) {
//		SpringApplication.run(DemoApplication.class, args);
		var app = new SpringApplication(DemoApplication.class);
		app.addInitializers(new ApplicationContextInitializer<GenericApplicationContext>() {
			@Override
			public void initialize(GenericApplicationContext ctx) {
				ctx.registerBean(OtherPack.class);
			}
		});
		app.run(args);
	}
}
