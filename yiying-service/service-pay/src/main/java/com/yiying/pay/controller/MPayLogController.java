package com.yiying.pay.controller;


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiying.common.JwtUtils;
import com.yiying.common.Result;
import com.yiying.config.QiException;
import com.yiying.movie.service.MMovieService;
import com.yiying.movie.vo.MovieVo;
import com.yiying.order.entity.MOrder;
import com.yiying.order.service.MOrderService;
import com.yiying.order.vo.OrderVo;
import com.yiying.pay.producer.PayProducer;
import com.yiying.pay.service.MPayLogService;
import com.yiying.pay.utils.ConstantPropertiesApliPayUtil;
import com.yiying.pay.vo.AlipayVo;
import com.yiying.pay.vo.OrderExt;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qitianfeng
 * @since 2020-10-24
 */
@Controller
@RequestMapping("/pay/")
public class MPayLogController {

    @Autowired
    private MPayLogService payLogService;

    @Reference
    private MOrderService orderService;

    @Reference
    private MMovieService mMovieService;

    @Autowired
    private PayProducer payProducer;


    @GetMapping("/alipay/{orderNo}")
    @ResponseBody
    public Result alipay(@PathVariable String orderNo, HttpServletResponse response) {

        OrderVo order = orderService.getByOrderNo(orderNo);
        if (order == null) {
            return Result.error();
        }
        String singnType = ConstantPropertiesApliPayUtil.SINGN_TYPE;
        AlipayClient alipayClient = new DefaultAlipayClient(ConstantPropertiesApliPayUtil.GATEWAY_URL,
                ConstantPropertiesApliPayUtil.APPID, ConstantPropertiesApliPayUtil.APP_PRIVATE_KEY, ConstantPropertiesApliPayUtil.FORMAT,
                "UTF-8",
                ConstantPropertiesApliPayUtil.APP_PUBLIC_KEY, "RSA2");

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //在公共参数中设置会跳和通知地址
        request.setReturnUrl(ConstantPropertiesApliPayUtil.RETURN_URL);
        request.setNotifyUrl(ConstantPropertiesApliPayUtil.NOTIFY_URL);

        String orderOrderNo = order.getOrderId();
        BigDecimal totalFee = order.getTotalFee();

        MovieVo movieInfo = mMovieService.getMovieInfo(order.getMovieId());


        AlipayVo alipayVo = new AlipayVo();
        alipayVo.setOut_trade_no(orderOrderNo);
        alipayVo.setProduct_code("FAST_INSTANT_TRADE_PAY");
        alipayVo.setSubject(movieInfo.getTitle());
        alipayVo.setTotal_amount(totalFee);
        String jsonString = JSON.toJSONString(alipayVo);

        request.setBizContent(jsonString);
        String form = null;

        try {
            form = alipayClient.pageExecute(request).getBody(); /// 调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

       /* response.setContentType("text/html;charset=utf-8");
        try {
            response.getWriter().write(form);
            response.getWriter().flush();
            response.getWriter().close();

        } catch (IOException e) {
            e.printStackTrace();
        }*/
//将表达返回
        return Result.ok().data("payForm", form);
    }

    @PostMapping("notify")
    @ResponseBody
    public String alipayNotify(HttpServletRequest request, String out_trade_no, String trade_no, String trade_status) {
        Map<String, String> paramsMap = getParamsMap(request);
        //验证签名
        try {
            boolean b = AlipaySignature.rsaCheckV1(paramsMap, ConstantPropertiesApliPayUtil.APP_PUBLIC_KEY, "utf-8", ConstantPropertiesApliPayUtil.SINGN_TYPE);
            if (b) {
                // 商户订单号
                String outTradeNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

                // 支付宝交易号
                String tradeNo = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

                // 付款金额
                String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

                System.out.println("商户订单号=" + outTradeNo);
                System.out.println("支付宝交易号=" + tradeNo);
                System.out.println("付款金额=" + total_amount);

                //支付成功，修复支付状态
                return "success";
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new QiException(20001, "跳转付款失败");
        }
        return "";
    }

    @GetMapping("return")
    public String alipayReturn(Map<String, String> params, HttpServletRequest request, String out_trade_no, String trade_no, String total_amount, ModelMap modelMap)
            throws AlipayApiException {
        Map<String, String> map = new HashMap<String, String>();
        map = this.getParamsMap(request);
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(map, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArFzFufIcT1bycfAjn5hmfCmQkMded+CdmHiSu1ze2FdlGpwj9cFhmnt+Ke4s/0pmQdT3mtoq1im/guiq5XhvWvFL6awVZsBgCmfwCMF7TGzzJIqE/Oo7j1CXR9trhQv6Wgq3tEAMJfU/vtRiRS6Hg6h60JtP1Z1+MwUb55yhXA5W7Wy2ohLft2YGZeoS62qsCYXW0juxCnX4gURf09klZ5fA/ItoO4qW9wK47KuUZ+VWIY8sbd82qdqUqJOFBQ+6VY4mU8601yKsEe6zL/frhKvb1VhTW1E2YPaazlXsKyZEnppSa56DHMAfInaTHC6jhxVCQheIO3dlR/DN+XXL9QIDAQAB", "UTF-8", "RSA2");//UTF-8要大写，不然验签失败
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "fail";// 验签发生异常,则直接返回失败
        }
        if (signVerified) {
            //更新支付状态
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            payLogService.updateOrderStatus(map,memberId);
            HashMap<String, String> hashMap = orderService.queryByOutTradeNo(out_trade_no);
            String movieId = hashMap.get("movieId");
            memberId = hashMap.get("memberId");
            modelMap.put("movieId", movieId);

            //发送异步消息，更新支付信息，更新订单状态
            OrderExt orderExt = new OrderExt();
            orderExt.setOrderNo(out_trade_no);
            orderExt.setMovieId(movieId);
            try {
                payProducer.sendAsyncMsgByJsonDelay("topic", orderExt);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "redirect:http://localhost:3000/movie/"+movieId;
        } else {
            System.out.println("验证失败,不去更新状态");
            return "fail";
        }
    }

    /*
        public String alipaynotify(Model model, HttpServletRequest request) {

    //        log.info("支付宝异步回调 ------------beg-----------");
            String result = "fail";
            //获取支付宝POST过来反馈信息
            *//* *
     * 功能：支付宝服务器异步通知页面
     * 说明：
     * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
     * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
     *//*
        Map<String, String> params=new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                System.out.println(valueStr);
            }
            params.put(name, valueStr);
        }
        boolean signVerified =false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, ConstantPropertiesApliPayUtil.APP_PUBLIC_KEY, "UTF-8", "RSA2");
        } catch (AlipayApiException e1) {
            // TODO Auto-generated catch block
//            log.error("由于"+e1.getErrMsg()+"返回给支付宝系统的结果result:fail");
            model.addAttribute("result", "fail");
            return result;
        } //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——

        *//* 实际验证过程建议商户务必添加以下校验：
        1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
        2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
        3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
        4、验证app_id是否为该商户本身。
        *//*
//        log.error("支付宝验证签名：---------------------------------"+signVerified);
        if(signVerified) {//验证成功
            //商户订单号
            //交易状态
//            log.info("支付宝异步回调验签成功！");
            String trade_status = params.get("trade_status");

            if("TRADE_FINISHED".equals(trade_status)){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                try {
                    // 在这里处理支付成功后的操作，比如修改订单状态等等
//                    coding...
                    result = "success";
                } catch (Exception e) {
//                    log.error(e.getMessage());
                    result = "fail";
                }
            }else if ("TRADE_SUCCESS".equals(trade_status)){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
                try {
                    // 在这里处理支付成功后的操作，比如修改订单状态等等
//                    coding...
                    result = "success";
                } catch (Exception e) {
//                    log.error(e.getMessage());
                    result = "fail";
                }
            }else{
                result = "fail";
            }
        }else {//验证失败
            result = "fail";
            //调试用，写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);
//            log.debug("支付宝异步回调验签失败");
        }
//        log.debug("异步回调返回给支付宝系统的结果result:"+result);

        model.addAttribute("result", result);
//        log.info("支付宝异步回调  -------------end ------------");
        return result;
    }*/
    private Map<String, String> getParamsMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            try {
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                params.put(name, valueStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return params;
    }

}

