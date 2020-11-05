package com.yiying.sso.controller;


import com.yiying.common.JwtUtils;
import com.yiying.common.Result;
import com.yiying.config.QiException;
import com.yiying.sso.entity.YiMember;
import com.yiying.sso.service.YiMemberService;
import com.yiying.sso.vo.LoginInfo;
import com.yiying.sso.vo.LoginVo;
import com.yiying.sso.vo.RegisterVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author qitianfeng
 * @since 2020-10-04
 */
@RestController
@RequestMapping("/sso")
public class MemberController {


    @Autowired
    private YiMemberService memberService;

    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo){
        String token =  memberService.login(loginVo);
        return Result.ok().data("token",token);
    }

    @PostMapping("register")
    public Result register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return Result.ok().message("注册成功");
    }

    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request){
        try {
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            LoginInfo loginInfoVo = memberService.getLoginInfo(memberId);
            return Result.ok().data("item", loginInfoVo);
        }catch (Exception e){
            e.printStackTrace();
            throw new QiException(20001,"error");
        }
    }

    //    //根据token获取用户信息

}

