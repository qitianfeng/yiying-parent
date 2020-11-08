package com.yiying.banner;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.yiying"})
public class ServiceBanner {
    public static void main(String[] args) {
        SpringApplication.run(ServiceBanner.class,args);
    }
}
