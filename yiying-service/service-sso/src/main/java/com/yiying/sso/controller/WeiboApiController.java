package com.yiying.sso.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiying.common.HttpclientUtil;
import com.yiying.common.JwtUtils;
import com.yiying.common.MD5;
import com.yiying.sso.entity.YiMember;
import com.yiying.sso.service.YiMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WeiboApiController {


    @Autowired
    YiMemberService memberService;

    @RequestMapping("vlogin")
    public String vlogin(String code, HttpServletRequest request) {
        // 授权码换取access_token
        // 换取access_token
        // client_secret=f043fe09dcab7e9b90cdd7491e282a8f
        // client_id=2173054083
        String s3 = "https://api.weibo.com/oauth2/access_token?";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("client_id", "2130291000");
        paramMap.put("client_secret", "8b85765221586f17f9ae27f38a128beb");
        paramMap.put("grant_type", "authorization_code");
        paramMap.put("redirect_uri", "http://passport.onlineMovie.com:2005/vlogin");
        paramMap.put("code", code);// 授权有效期内可以使用，没新生成一次授权码，说明用户对第三方数据进行重启授权，之前的access_token和授权码全部过期
        String access_token_json = HttpclientUtil.doPost(s3, paramMap);

        Map<String, Object> access_map = JSON.parseObject(access_token_json, Map.class);

        // access_token换取用户信息
        String uid = (String) access_map.get("uid");
        String access_token = (String) access_map.get("access_token");
        String show_user_url = "https://api.weibo.com/2/users/show.json?access_token=" + access_token + "&uid=" + uid;
        String user_json = HttpclientUtil.doGet(show_user_url);
        Map<String, Object> user_map = JSON.parseObject(user_json, Map.class);

        // 将用户信息保存数据库，用户类型设置为微博用户
        YiMember member = new YiMember();
        member.setNickname((String) user_map.get("screen_name"));
        String profile_image_url = (String) user_map.get("profile_image_url");
        member.setAvatar(profile_image_url);
        Integer g = 1;
        String gender = (String) user_map.get("gender");

        if (gender.equals("m")) {
            g = 2;
        }
        member.setSex(g);
        member.setPassword(MD5.encrypt("123456"));
        member.setOpenid((String) user_map.get("idstr"));
        member.setIsDisabled(0);
        member.setIsDeleted(0);


        YiMember umsCheck = new YiMember();
        umsCheck.setOpenid(member.getOpenid());
        LambdaQueryWrapper<YiMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YiMember::getOpenid,member.getOpenid());
        YiMember one = memberService.getOne(wrapper);

        if (one== null) {
            memberService.save(member);
        } else {
            member = one;
        }


/*
        String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();// 从request中获取ip
            if (StringUtils.isBlank(ip)) {
                ip = "127.0.0.1";
            }
        }
*/

        // 按照设计的算法对参数进行加密后，生成token
        String jwtToken = JwtUtils.setJwtToken(member.getId(), member.getNickname());
        return "redirect:http://localhost:3000?token=" + jwtToken;
    }
}

