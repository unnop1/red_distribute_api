package com.nt.red_distribute_api;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class RedDistributeApiApplication extends SpringBootServletInitializer{
    public static void main(String[] args) {
        SpringApplication.run(RedDistributeApiApplication.class, args);
    }
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
