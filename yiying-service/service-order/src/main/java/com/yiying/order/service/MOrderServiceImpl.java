package com.yiying.order.service;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiying.common.IdWorker;
import com.yiying.movie.dto.PlayHallSeat;
import com.yiying.movie.entity.MMovie;
import com.yiying.movie.entity.MMoviePlayHall;
import com.yiying.movie.entity.MPlayHall;
import com.yiying.movie.entity.MSubject;
import com.yiying.movie.service.MMoviePlayHallService;
import com.yiying.movie.service.MMovieService;
import com.yiying.movie.service.MPlayHallService;
import com.yiying.movie.service.MSubjectService;
import com.yiying.movie.vo.MovieVo;
import com.yiying.order.entity.MOrder;
import com.yiying.order.mapper.MOrderMapper;
import com.yiying.order.vo.OrderTicketVo;
import com.yiying.order.vo.OrderVo;
import com.yiying.order.vo.Params;
import com.yiying.sso.service.YiMemberService;
import com.yiying.sso.vo.LoginInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qitianfeng
 * @since 2020-10-24
 */
@Service
public class MOrderServiceImpl extends ServiceImpl<MOrderMapper, MOrder> implements MOrderService {

    @Reference
    private MMovieService movieService;

    @Reference(check = false, init = true)
    private YiMemberService memberService;


    @Override
    public String createOrder(String movieId, String jwtToken) {
        MOrder order = getOrder(movieId, jwtToken);

        //这里可以设置扣库存

        //扣优惠券 ...

        return order.getOrderId();
    }

    @Override
    public Boolean haveBuy(String memberId, String movieId) {
        LambdaQueryWrapper<MOrder> wrapper = new LambdaQueryWrapper<MOrder>().eq(MOrder::getMovieId, movieId).eq(MOrder::getMemberId, memberId);
        wrapper.eq(MOrder::getStatus, 1);
        int count = this.count(wrapper);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public OrderVo getByOrderNo(String orderNo) {
        LambdaQueryWrapper<MOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MOrder::getOrderId, orderNo);
        MOrder order = this.getOne(wrapper);
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);
        return orderVo;
    }

    /**
     * 根据订单id查询电影id
     *
     * @param out_trade_no
     * @return
     */
    @Override
    public HashMap<String, String> queryByOutTradeNo(String out_trade_no) {

        LambdaQueryWrapper<MOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MOrder::getOrderId, out_trade_no);
        MOrder order = this.getOne(wrapper);
       /* Arrays.sort(new ArrayList<String>(), new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return 0;
            }
        });*/
        String memberId = order.getMemberId();
        HashMap<String, String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("movieId",order.getMovieId());
        return map;
    }

    /**
     * 创建购买电影票的订单
     *
     * @param movieId
     * @param jwtToken
     * @return
     */
    @Override
    public String createOrdersTicket(String movieId, String jwtToken) {
        MOrder order = getOrder(movieId, jwtToken);

        //这里可以设置扣库存

        //扣优惠券 ...

        return order.getOrderId();
    }


    @Reference
    private MMoviePlayHallService moviePlayHallService;
    @Reference
    private MPlayHallService playHallService;
    @Reference
    private MSubjectService subjectService;

    /**
     * 填充购买电影票的信息展示
     * 保存并更新座位信息
     * 列举出电影的播放影厅、场次
     *
     * @param orderId
     * @return
     */
    @Override
    public Map<String, Object> getOrderInfoByTicket(String orderId) {

        LambdaQueryWrapper<MOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MOrder::getOrderId, orderId);
        MOrder order = this.getOne(wrapper);

        if (order.getStatus() == 1) {
            try {
                throw new Exception("请不要重新下单，该订单已经被消费！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String movieId = order.getMovieId();
        String memberId = order.getMemberId();
        OrderTicketVo orderTicketVo = new OrderTicketVo();
        MMovie byId = movieService.getById(movieId);
        BeanUtils.copyProperties(byId, orderTicketVo);


        MSubject subject = subjectService.getTitleById(byId.getSubjectGeneresId());

        orderTicketVo.setGenres(subject.getGenres());

        //获取电影展厅的名称
        MMoviePlayHall moviePlayHall = moviePlayHallService.getPlayHall(movieId);
        //获得播放厅的id，根据播放厅的id查询相应的播放厅名称
        MPlayHall playHall = playHallService.getById(moviePlayHall.getWatchHallId());

        //拼接场次信息，放映日期+播放厅的名称
        orderTicketVo.setChangci(byId.getReleaseDate() + "---" + playHall.getTitle());

        orderTicketVo.setPlayHall(playHall.getTitle());
        //获取电影的位置信息
        String seats = moviePlayHall.getSeats();

        //转化为二级数组
        ArrayList list = JSON.parseObject(seats, ArrayList.class);

        int[][] seat = new int[10][10];

        for (int i = 0; i < list.size(); i++) {
            String s1 = JSON.toJSONString(list.get(i));
            PlayHallSeat playHallSeat = JSON.parseObject(s1, PlayHallSeat.class);
            System.out.println(playHallSeat);
            int x = Integer.parseInt(playHallSeat.getSeatsRow()) - 1;
            int y = Integer.parseInt(playHallSeat.getSeatsColumn()) - 1;
            seat[x][y] = Integer.parseInt(playHallSeat.getStatus());
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("seats", seat);
        for (int[] ints : seat) {
            System.out.print(ints);
        }
        map.put("movieMessage", orderTicketVo);
        return map;
    }

    /**
     * 修改订单的信息
     *
     * @param orderId
     * @param memberId
     * @param params
     */
    @Override
    public void modifyTicketOrder(String orderId, String memberId, Params params) {
        LambdaQueryWrapper<MOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MOrder::getOrderId, orderId);
        wrapper.eq(MOrder::getMemberId, memberId);
        MOrder order = this.getOne(wrapper);
        ///修改订单的价格
        order.setTotalFee(order.getTotalFee().multiply(new BigDecimal(params.getLength())));

        //更新订单的座位信息
        ArrayList<PlayHallSeat> playHallSeats = new ArrayList<>();
        PlayHallSeat playHallSeat = null;
        int[][] list = params.getMsg();
        ;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (list[i][j] == (2)) {
                    playHallSeat = new PlayHallSeat();
                    playHallSeat.setSeatsRow(i + 1 + "");
                    playHallSeat.setSeatsColumn(j + 1 + "");
                    playHallSeat.setStatus("2");
                    playHallSeats.add(playHallSeat);
                }
            }
        }
        String seats = JSON.toJSONString(playHallSeats);
        order.setSeats(seats);

        //更新影院的座位表信息
        MMoviePlayHall moviePlayHall = moviePlayHallService.getOneByMovieId(order.getMovieId());

        ArrayList<PlayHallSeat> seats1 = new ArrayList<>();
        playHallSeat = null;
        int[][] list1 = params.getList();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                playHallSeat = new PlayHallSeat();
                playHallSeat.setSeatsRow(String.valueOf(i + 1));
                playHallSeat.setSeatsColumn(String.valueOf(j + 1));
                playHallSeat.setStatus(String.valueOf(list1[i][j]));
                seats1.add(playHallSeat);
            }
        }

        String s = JSON.toJSONString(seats1);
        moviePlayHall.setSeats(s);

        moviePlayHallService.update(moviePlayHall, null);

        boolean update = this.update(order, null);

        System.out.println(update);
    }

    @Override
    public MOrder getOneById(String out_trade_id) {
        LambdaQueryWrapper<MOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MOrder::getOrderId, out_trade_id);
        return this.getOne(wrapper);
    }

    private MOrder getOrder(String movieId, String jwtToken) {
        synchronized (this) {
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
            return order;
        }
    }

    public static void main(String[] args) {
        System.out.println(RandomUtil.randomString(30));
    }
}
