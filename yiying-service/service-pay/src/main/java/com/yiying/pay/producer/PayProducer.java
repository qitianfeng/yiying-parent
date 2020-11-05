package com.yiying.pay.producer;

import com.yiying.pay.vo.OrderExt;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.Charset;

@Component
public class PayProducer {

    private static Logger logger = Logger.getLogger(PayProducer.class);
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送异步延迟消息
     * RocketMQ的延迟消息实现非常简单，只需要发送消息前设置延迟的时间，延迟时间存在十八个等级
     * （1s/5s/10s/30s/1m/2m/3m/4m/5m/6m/7m/8m/9m/10m/20m/30m/1h/2h），调用setDelayTimeLevel()设置
     * 与时间相对应的延迟级别即可
     */
    public void sendAsyncMsgByJsonDelay(String topic, OrderExt orderExt) throws Exception {
        //将order转为json字符串
        String s = rocketMQTemplate.getObjectMapper().writeValueAsString(orderExt);
        org.apache.rocketmq.common.message.Message message = new
                org.apache.rocketmq.common.message.Message(topic, "order",s.getBytes(Charset.forName("utf-8")));

        //设置延迟等级
        message.setDelayTimeLevel(1);

        DefaultMQProducer producer = null;
        //实例化消息生产者Producer
        producer = new DefaultMQProducer("${rocketmq.producer.group}");
        //设置nameserver
        producer.setNamesrvAddr("123.57.11.24:9876");

        rocketMQTemplate.setProducer(producer);
        logger.error(rocketMQTemplate.getProducer().toString());
        //发送异步消息
        rocketMQTemplate.getProducer().send(message, new SendCallback() {

            @Override
            public void onSuccess(SendResult sendResult) {

            }

            @Override
            public void onException(Throwable throwable) {
                //支付失败回滚订单

                System.out.println(throwable.getMessage());
            }
        });
    }

}
