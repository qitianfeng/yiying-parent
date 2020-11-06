package com.yiying.sso.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiying.common.JwtUtils;
import com.yiying.common.MD5;
import com.yiying.config.QiException;
import com.yiying.order.entity.MOrder;
import com.yiying.order.service.MOrderService;
import com.yiying.sso.entity.YiMember;
import com.yiying.sso.mapper.YiMemberMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiying.sso.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author qitianfeng
 * @since 2020-10-04
 */
@Service(delay = 10000)
public class YiMemberServiceImpl extends ServiceImpl<YiMemberMapper, YiMember> implements YiMemberService {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

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
            throw new QiException(20001, "手机号不存在");
        }
        //获取用户相关信息并作出相应判断
        LambdaQueryWrapper<YiMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YiMember::getMobile, mobile);
        YiMember member = baseMapper.selectOne(wrapper);

        if (member == null) {
            throw new QiException(20001, "用户不存在");
        }

        if (!MD5.encrypt(password).equals(member.getPassword())) {
            throw new QiException(20001, "密码不正确");
        }

        //判断是否被禁用
        if (member.getIsDisabled() == 1) {
            throw new QiException(20001, "用户已被禁用");
        }
        //判断是否被禁用删除
        if (member.getIsDeleted() == 1) {
            throw new QiException(20001, "用户已被删除");
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
        wrapper.eq(YiMember::getMobile, mobile);
        YiMember member = this.getOne(wrapper);
        if (member != null) {
            throw new QiException(20001, "手机号已被注册！");
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
        BeanUtils.copyProperties(yiMember, loginInfo);
        return loginInfo;
    }

    @Reference(check = false,init = true)
    private MOrderService orderService;

    /**
     * 查询会员的订单信息
     *
     * @param memberId
     * @return
     */
    @Override
    public List<MemberOrder> queryOrder(String memberId) {

        LambdaQueryWrapper<MOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MOrder::getMemberId, memberId);

        List<MOrder> orderList = orderService.list(wrapper);
        MemberOrder memberOrder = null;

        ArrayList<MemberOrder> memberOrders = new ArrayList<>();
        for (MOrder order : orderList) {
            memberOrder = new MemberOrder();
            BeanUtils.copyProperties(order, memberOrder);
            if (order.getSeats() != null) {
                String seats = order.getSeats();
                ArrayList list = JSON.parseObject(seats, ArrayList.class);
                int[][] seat = new int[10][10];

                for (int i = 0; i < list.size(); i++) {
                    String s1 = JSON.toJSONString(list.get(i));
                    PlayHallSeat playHallSeat = JSON.parseObject(s1, PlayHallSeat.class);
                    System.out.println(playHallSeat);
                    int x = Integer.parseInt(playHallSeat.getSeatsRow()) - 1;
                    int y = Integer.parseInt(playHallSeat.getSeatsColumn()) - 1;
                    memberOrder.setSeat(x + " 排 |" + y + " 列");
                    memberOrders.add(memberOrder);
                }
            }
            memberOrders.add(memberOrder);
        }
        return memberOrders;
    }

    /**
     * 修改用户密码
     *
     * @param memberId
     * @param memberPassword
     * @return
     */
    @Override
    public Boolean modifiedSecret(String memberId, MemberPassword memberPassword) {

        LambdaQueryWrapper<YiMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YiMember::getId, memberId);
        YiMember member = this.getOne(wrapper);
        //判断密码是否输入正确
        if (!member.getPassword().equalsIgnoreCase(MD5.encrypt(memberPassword.getOldSecret()))) {
            return false;
        }
        member.setPassword(MD5.encrypt(memberPassword.getNewSecret()));

        //更新密码
        boolean update = this.update(member, null);


        return update;
    }
}
