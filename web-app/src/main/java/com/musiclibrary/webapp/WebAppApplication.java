package com.musiclibrary.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class WebAppApplication extends SpringBootServletInitializer {
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder app) {
        return app.sources(WebAppApplication.class);
    }
    
    public static void main(String[] args) {
        SpringApplication.run(WebAppApplication.class, args);
        System.out.println("*** WEB APPLICATION STARTED ***");
        System.out.println("*** Home: http://localhost:9090 ***");
        System.out.println("*** Admin: http://localhost:9090/admin ***");
        System.out.println("*** User: http://localhost:9090/user/login ***");
    }
}
