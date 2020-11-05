package com.yiying.pay.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class AlipayVo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 订单名称
     */
    private String subject;
    /**
     * 商户网站唯一订单号
     */
    private String out_trade_no;
    /**
     * 该笔订单允许的最晚付款时间
     */
    private String timeout_express;
    /**
     * 付款金额
     */
    private BigDecimal total_amount;
    /**
     * 销售产品码，与支付宝签约的产品码名称
     */
    private String product_code;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTimeout_express() {
        return timeout_express;
    }

    public void setTimeout_express(String timeout_express) {
        this.timeout_express = timeout_express;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}
