package com.nt.red_distribute_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class RedDistributeApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(RedDistributeApiApplication.class, args);
    }

    // //If you need a traditional war deployment we need to extend the SpringBootServletInitializer
    // //This helps us deploy war files to Jboss containers
    // @Override
    // protected SpringApplicationBuilder configure(SpringApplicationBuilder springApplicationBuilder) {
    //     return springApplicationBuilder.sources(RedDistributeApiApplication.class);
    // }
}