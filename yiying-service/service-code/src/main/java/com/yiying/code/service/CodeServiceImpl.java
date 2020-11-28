package com.yiying.code.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author zzf
 * @version 1.0
 * @date 2020/11/28 14:01
 */
@Service
public class CodeServiceImpl implements CodeService {
    @Value("${host}")
    String host;
    @Value("${from}")
    String from;
    @Value("${user}")
    String user;
    @Value("${pass}")
    String pass;

    @Autowired
    RedisTemplate redisTemplate;

    //发送验证码到邮箱
    @Override
    public String sendCode(String mobile, String mail) {
        MailAccount account = new MailAccount();
        String code = RandomUtil.randomString(6);
        account.setHost(host);
        account.setPort(25);
        account.setAuth(true);
        account.setFrom(from);
        account.setUser(user);
        account.setPass(pass);
        //10分钟后失效
        redisTemplate.opsForValue().set(mobile, code, 60 * 10);
        MailUtil.send(account, CollUtil.newArrayList(mail), "验证码", "验证码在十分钟内有效, 验证码："+code, false);
        return code;
    }


}
