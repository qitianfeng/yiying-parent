package com.yiying.code.controller;

import cn.hutool.extra.mail.MailUtil;
import com.yiying.code.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zzf
 * @version 1.0
 * @date 2020/11/28 14:03
 */
@RestController
@RequestMapping("code")
public class CodeController {

    @Autowired
    private CodeService codeService;

    @GetMapping("/send")
    public void send(){

        codeService.sendCode("13642440515","1454429051@qq.com");
    }
}
