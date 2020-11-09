package com.yiying.sso.controller;


import com.yiying.common.JwtUtils;
import com.yiying.common.Result;
import com.yiying.config.QiException;
import com.yiying.sso.entity.YiMember;
import com.yiying.sso.service.YiMemberService;
import com.yiying.sso.vo.*;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public Result login(@RequestBody LoginVo loginVo) {
        String token = memberService.login(loginVo);
        return Result.ok().data("token", token);
    }

    @PostMapping("register")
    public Result register(@RequestBody RegisterVo registerVo) {
        memberService.register(registerVo);
        return Result.ok().message("注册成功");
    }

    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request) {
        try {
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            LoginInfo loginInfoVo = memberService.getLoginInfo(memberId);
            return Result.ok().data("item", loginInfoVo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new QiException(20001, "error");
        }
    }

    //查询会员的订单信息
    public Result queryOrder(HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        List<MemberOrder> orderVoList = memberService.queryOrder(memberId);
        return Result.ok().data("orderList", orderVoList);
    }

    //用户修改信息
    public Result modifyMsg(@RequestBody YiMemberVo yiMemberVo){
        YiMember yiMember = new YiMember();
        BeanUtils.copyProperties(yiMemberVo,yiMember);
        memberService.updateById(yiMember);
        return Result.ok();
    }

    //用户修改密码
    public Result modifiedSecret(@RequestBody MemberPassword memberPassword,HttpServletRequest request){

        String memberId = JwtUtils.getMemberIdByJwtToken(request);

        Boolean v =  memberService.modifiedSecret(memberId,memberPassword);
        if (v){
            return Result.ok();
        }
        return Result.error();
    }

}

