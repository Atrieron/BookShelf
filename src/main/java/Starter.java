package main.java;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.vaadin.spring.annotation.EnableVaadin;

@ServletComponentScan
@SpringBootApplication
@EnableVaadin
public class Starter extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(Starter.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Starter.class);
    }
	
	private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
        return builder.sources(Starter.class);
    }
}