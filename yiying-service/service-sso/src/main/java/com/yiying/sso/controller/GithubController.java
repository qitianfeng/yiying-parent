package com.yiying.sso.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yiying.common.HttpclientUtil;
import com.yiying.common.JwtUtils;
import com.yiying.common.MD5;
import com.yiying.common.Result;
import com.yiying.sso.entity.YiMember;
import com.yiying.sso.service.YiMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
@Controller
@RequestMapping("/sso")
public class GithubController {
    @Autowired
    YiMemberService memberService;
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("oauth/redirect")
    public String rediret(@RequestParam("code") String code,String state) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", "8385cc46ac105b65f503");
        map.put("client_secret", "476136738e78168ddfef55e4ea33ce03648d32d7");
        map.put("state", "randomState");
        map.put("code", code);
        map.put("redirect_uri", "http://localhost:2005/sso/oauth/redirect");
        Map<String, String> resp = restTemplate.postForObject("https://github.com/login/oauth/access_token", map, Map.class);
        System.out.println(resp);
        HttpHeaders httpheaders = new HttpHeaders();
        httpheaders.add("Authorization", "token " + resp.get("access_token"));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpheaders);
        ResponseEntity<Map> exchange = restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, httpEntity, Map.class);
        System.out.println("exchange.getBody() = " + new ObjectMapper().writeValueAsString(exchange.getBody()));
        // request.setAttribute("exchange", exchange);
        Map body = exchange.getBody();

        YiMember member = new YiMember();
        member.setNickname((String) body.get("login"));
        String avatar_url = (String) body.get("avatar_url");
        member.setAvatar(avatar_url);
        member.setPassword(MD5.encrypt("123456"));
        member.setIsDisabled(0);


        LambdaQueryWrapper<YiMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YiMember::getAvatar, member.getAvatar());
        YiMember one = memberService.getOne(wrapper);

        if (one == null) {
            memberService.save(member);
        } else {
            member = one;
        }


        // 按照设计的算法对参数进行加密后，生成token
        String jwtToken = JwtUtils.setJwtToken(member.getId(), member.getNickname());

        return "redirect:http://localhost:3000?token=" + jwtToken;
    }
}
