package com.yiying.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zzf
 * @version 1.0
 * @date 2020/11/28 13:40
 */

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.yiying"})
public class ServiceCode {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCode.class,args);
    }

}
