package it.studiomascia.gestionale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class GestionaleApplication extends SpringBootServletInitializer{
 
    public static void main(String[] args) {
            SpringApplication.run(GestionaleApplication.class, args);
    }
    
    @Override
     protected SpringApplicationBuilder configure(SpringApplicationBuilder app) {
             return app.sources(GestionaleApplication.class);
         }
}


 