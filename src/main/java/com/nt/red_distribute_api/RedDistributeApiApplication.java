package com.nt.red_distribute_api;

import org.modelmapper.ModelMapper;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class RedDistributeApiApplication extends SpringBootServletInitializer{

	static {
        // Remove existing handlers attached to the j.u.l root logger
        java.util.logging.LogManager.getLogManager().reset();
        // Bridge JUL to SLF4J
        SLF4JBridgeHandler.install();
    }
	public static void main(String[] args) {
        SpringApplication.run(RedDistributeApiApplication.class, args);
    }
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
