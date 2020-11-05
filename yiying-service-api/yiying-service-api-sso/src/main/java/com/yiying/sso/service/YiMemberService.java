package com.yiying.sso.service;

import com.yiying.sso.entity.YiMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yiying.sso.vo.LoginInfo;
import com.yiying.sso.vo.LoginVo;
import com.yiying.sso.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author qitianfeng
 * @since 2020-10-04
 */
public interface YiMemberService extends IService<YiMember> {

    /**
     * 用户登录接口
     * @param loginVo
     * @return
     */
    String login(LoginVo loginVo);

    /**
     * 用户注册接口
     * @param registerVo
     */
    void register(RegisterVo registerVo);

    /**
     * 获取登录用户的信息
     * @param memberId
     * @return
     */
    LoginInfo getLoginInfo(String memberId);
}
