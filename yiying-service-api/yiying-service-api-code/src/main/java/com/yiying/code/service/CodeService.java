package com.yiying.code.service;

/**
 * @author zzf
 * @version 1.0
 * @date 2020/11/28 13:53
 */
public interface CodeService {


    //发送验证码到邮箱
    String sendCode(String mobile, String mail);
}
