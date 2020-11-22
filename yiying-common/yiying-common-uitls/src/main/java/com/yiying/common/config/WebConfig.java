//package com.yiying.common.config;
//
//import com.yiying.common.interceptor.MyInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
///**
// * @author zzf
// * @version 1.0
// * @date 2020/10/18 21:02
// */
//@Configuration
//public class WebConfig extends WebMvcConfigurerAdapter {
//
//
//    /**
//     * 将自定义的interceptor注入到springboot中
//     */
//    @Autowired
//    private MyInterceptor interceptor;
//
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(interceptor);
//    }
//}
