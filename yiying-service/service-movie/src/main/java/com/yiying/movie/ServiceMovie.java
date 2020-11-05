package com.yiying.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.yiying"})
public class ServiceMovie {
    public static void main(String[] args) {
        SpringApplication.run(ServiceMovie.class,args);
    }
}
