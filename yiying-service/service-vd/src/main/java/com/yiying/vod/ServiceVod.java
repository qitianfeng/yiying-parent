package com.yiying.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.yiying"})
public class ServiceVod {
    public static void main(String[] args) {
        SpringApplication.run(ServiceVod.class,args);
    }
}
