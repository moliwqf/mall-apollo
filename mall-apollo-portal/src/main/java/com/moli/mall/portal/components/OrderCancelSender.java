package com.moli.mall.portal.components;

import com.moli.mall.portal.constant.enums.QueueEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author moli
 * @time 2024-04-08 23:12:02
 * @description 取消订单消息的发出者
 */
@Component
public class OrderCancelSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCancelSender.class);

    @Resource
    private AmqpTemplate amqpTemplate;

    public void sendMessage(Long orderId, final long delayTimes) {
        amqpTemplate.convertAndSend(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange(), QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey(), orderId, msgProcessor -> {
            msgProcessor.getMessageProperties().setExpiration(String.valueOf(delayTimes));
            return msgProcessor;
        });
        LOGGER.info("send orderId:{}",orderId);
    }
}
