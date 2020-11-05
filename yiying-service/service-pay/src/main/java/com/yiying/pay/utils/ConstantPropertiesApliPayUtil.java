package com.yiying.pay.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantPropertiesApliPayUtil implements InitializingBean {

    @Value("${alipay.pay.APPID}")
    private String appid;

    @Value("${alipay.pay.APP_PRIVATE_KEY}")
    private String appPrivateKey;

    @Value("${alipay.pay.APP_PUBLIC_KEY}")
    private String appPublicKey;

    @Value("${alipay.pay.GATEWAY_URL}")
    private String gatewayUrl;

    @Value("${alipay.pay.SINGN_TYPE}")
    private String singnType;
    @Value("${alipay.pay.FORMAT}")
    private String format;

    @Value("${alipay.pay.RETURN_URL}")
    private String returnUrl;


    @Value("${alipay.pay.NOTIFY_URL}")
    private String notifyurl;


    public static String APPID;
    public static String APP_PRIVATE_KEY;
    public static String APP_PUBLIC_KEY;
    public static String GATEWAY_URL;
    public static String FORMAT;
    public static String SINGN_TYPE;
    public static String NOTIFY_URL;
    public static String RETURN_URL;

    @Override
    public void  afterPropertiesSet() throws Exception {
        APPID = appid;
        APP_PRIVATE_KEY = appPrivateKey;
        APP_PUBLIC_KEY = appPublicKey;
        GATEWAY_URL = gatewayUrl;
        FORMAT = format;
        singnType = SINGN_TYPE;
        NOTIFY_URL = notifyurl;
        RETURN_URL = returnUrl;

    }
}
