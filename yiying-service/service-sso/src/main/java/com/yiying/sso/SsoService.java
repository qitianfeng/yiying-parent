package com.yiying.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.yiying"})
@EnableDiscoveryClient
public class SsoService {
    public static void main(String[] args) {
        SpringApplication.run(SsoService.class,args);
    }
}
