package com.moli.mall.portal.components;

import com.moli.mall.portal.service.OmsPortalOrderService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author moli
 * @time 2024-04-08 23:19:58
 * @description 取消订单消息的处理者
 */
@Component
@RabbitListener(queues = "mall.order.cancel")
public class OrderCancelReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCancelReceiver.class);

    @Resource
    private OmsPortalOrderService portalOrderService;

    @RabbitHandler
    public void handle(Long orderId) {
        // 取消订单
        portalOrderService.cancelOrder(orderId);

        LOGGER.info("process orderId:{}", orderId);
    }
}
