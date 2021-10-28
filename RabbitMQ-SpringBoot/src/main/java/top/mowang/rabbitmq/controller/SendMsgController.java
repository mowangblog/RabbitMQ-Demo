package top.mowang.rabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mowang.rabbitmq.config.DelayedQueueConfig;

/**
 * RabbitMQ-Demo
 *
 * @author : Xuan Li
 * @website : https://mowangblog.top
 * @date : 2021/10/28 11:39
 **/
@Slf4j
@RestController
@RequestMapping("ttl")
@SuppressWarnings("all")
public class SendMsgController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/send/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("服务器接收到消息{}，准备发送到消息给两个TTL队列", message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自延迟十秒的队列：" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自延迟四十秒的队列：" + message);
    }

    @GetMapping("/sendExpirationMsg/{message}/{time}")
    public void sendMsg(@PathVariable String message,
                        @PathVariable String time) {
        log.info("服务器接收到消息{}，延迟{}秒发送到队列C", message, time);
        rabbitTemplate.convertAndSend("X", "XC",
                "队列C接受延迟" + time + "毫秒消息：" + message,
                msg -> {
                    msg.getMessageProperties().setExpiration(time);
                    return msg;
                });
    }

    //发送基于插件的延迟消息
    @GetMapping("/sendDelayedMsg/{message}/{time}")
    public void sendDelayedMsg(@PathVariable String message,
                        @PathVariable Integer time) {
        log.info("服务器接收到消息{}，延迟{}秒发送到延迟队列", message, time);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY,
                "延迟队列接受延迟" + time + "毫秒消息：" + message,
                msg -> {
                    msg.getMessageProperties().setDelay(time);
                    return msg;
                });
    }
}
