package me.sangjin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtherConfiguration {
    @Bean
    public Other other(){
        Other other = new Other();
        other.setName("Sangjin");
        other.setAge(26);
        return other;
    }
}
