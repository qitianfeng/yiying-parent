package com.yiying.search;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class,scanBasePackages={ "com.yiying"})
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.yiying"})
public class ServiceSearch {
    public static void main(String[] args) {
        //Caused by: java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(ServiceSearch.class, args);

    }
}
