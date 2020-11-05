package com.yiying.sso.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiying.common.JwtUtils;
import com.yiying.common.MD5;
import com.yiying.config.QiException;
import com.yiying.sso.entity.YiMember;
import com.yiying.sso.mapper.YiMemberMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiying.sso.vo.LoginInfo;
import com.yiying.sso.vo.LoginVo;
import com.yiying.sso.vo.RegisterVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author qitianfeng
 * @since 2020-10-04
 */
@Service
public class YiMemberServiceImpl extends ServiceImpl<YiMemberMapper, YiMember> implements YiMemberService {

    @Autowired
    RedisTemplate<String,String> redisTemplate;
    /**
     * 用户登录
     *
     * @param loginVo
     * @return
     */
    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        if (StringUtils.isEmpty(mobile)) {
            throw new QiException(20001,"手机号不存在");
        }
        //获取用户相关信息并作出相应判断
        LambdaQueryWrapper<YiMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YiMember::getMobile,mobile);
        YiMember member = baseMapper.selectOne(wrapper);

        if (member == null) {
            throw new QiException(20001,"用户不存在");
        }

        if (!MD5.encrypt(password).equals(member.getPassword())) {
            throw new QiException(20001,"密码不正确");
        }

        //判断是否被禁用
        if (member.getIsDisabled() == 1) {
            throw new QiException(20001,"用户已被禁用");
        }
        //判断是否被禁用删除
        if (member.getIsDeleted() == 1) {
            throw new QiException(20001,"用户已被删除");
        }

        //设置jwt生成token并返回
        String jwtToken = JwtUtils.setJwtToken(member.getId(), member.getNickname());

        return jwtToken;
    }

    /**
     * 用户注册
     *
     * @param registerVo
     */
    @Override
    public void register(RegisterVo registerVo) {
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        //获取用户相关信息并作出相应判断
        LambdaQueryWrapper<YiMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YiMember::getMobile,mobile);
        YiMember member = this.getOne(wrapper);
        if (member != null) {
            throw new QiException(20001,"手机号已被注册！");
        }

        //获取Redis相应的验证码
       /* String codeFromRedis = redisTemplate.opsForValue().get(registerVo.getMobile());
        if (!code.equals(codeFromRedis)) {
            throw new QiException(20001,"验证码不正确！");
        }*/

        YiMember yiMember = new YiMember();
        yiMember.setPassword(MD5.encrypt(registerVo.getPassword()));
        yiMember.setMobile(mobile);
        yiMember.setNickname(registerVo.getNickname());
        yiMember.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        baseMapper.insert(yiMember);
    }

    /**
     * 获取用户信息
     *
     * @param memberId
     * @return
     */
    @Override
    public LoginInfo getLoginInfo(String memberId) {
        YiMember yiMember = baseMapper.selectById(memberId);
        LoginInfo loginInfo = new LoginInfo();
        BeanUtils.copyProperties(yiMember,loginInfo);
        return loginInfo;
    }
}
