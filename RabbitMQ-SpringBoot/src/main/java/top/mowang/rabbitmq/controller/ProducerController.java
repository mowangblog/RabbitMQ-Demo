package top.mowang.rabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RabbitMQ-Demo
 *
 * @author : Xuan Li
 * @website : https://mowangblog.top
 * @date : 2021/10/28 16:01
 **/
@Slf4j
@RestController
@RequestMapping("/confirm")
@SuppressWarnings("all")
public class ProducerController {
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("sendMessage/{message}")
    public void sendMessage(@PathVariable String message){
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME,"key1",message,new CorrelationData("1"));
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME,"key2",message,new CorrelationData("2"));
        log.info("发送消息内容:{}",message);
    }
}
