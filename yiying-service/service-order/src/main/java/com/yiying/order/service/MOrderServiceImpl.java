package com.yiying.order.service;

import cn.hutool.Hutool;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiying.common.IdWorker;
import com.yiying.movie.service.MMovieService;
import com.yiying.movie.vo.MovieVo;
import com.yiying.order.entity.MOrder;
import com.yiying.order.mapper.MOrderMapper;
import com.yiying.order.service.MOrderService;
import com.yiying.sso.service.YiMemberService;
import com.yiying.sso.vo.LoginInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qitianfeng
 * @since 2020-10-24
 */
@Service
public class MOrderServiceImpl extends ServiceImpl<MOrderMapper, MOrder> implements MOrderService {

    @Reference
    private MMovieService movieService;

    @Reference
    private YiMemberService memberService;


    @Override
    public String createOrder(String movieId, String jwtToken) {
        //获取电影信息
        MovieVo movieInfo = movieService.getMovieInfo(movieId);
        //获取用户信息
        LoginInfo loginInfo = memberService.getLoginInfo(jwtToken);


        //创建订单
        MOrder order = new MOrder();
        order.setOrderId(String.valueOf(new IdWorker().nextId()));
        order.setMovieId(movieId);
        order.setMovieTitle(movieInfo.getTitle());
        order.setMoviePoster(movieInfo.getPoster());
        order.setTotalFee(movieInfo.getPrice());
        order.setMemberId(jwtToken);
        order.setMobile(loginInfo.getMobile());
        order.setNickname(loginInfo.getNickname());
        order.setStatus(0);
        order.setPayType(1);
        this.save(order);

        //这里可以设置扣库存

        //扣优惠券 ...

        return order.getOrderId();
    }
    @Override
    public Boolean haveBuy(String memberId, String movieId){
        LambdaQueryWrapper<MOrder> wrapper = new LambdaQueryWrapper<MOrder>().eq(MOrder::getMovieId, movieId).eq(MOrder::getMemberId, memberId);
        wrapper.eq(MOrder::getStatus,1);
        int count = this.count(wrapper);
        if (count > 0) {
            return true;
        }
        return false;
    }
}
