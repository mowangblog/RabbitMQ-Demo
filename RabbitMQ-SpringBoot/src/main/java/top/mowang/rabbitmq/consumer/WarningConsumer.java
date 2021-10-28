package top.mowang.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.mowang.rabbitmq.config.ConfirmConfig;

/**
 * RabbitMQ-Demo
 *
 * @author : Xuan Li
 * @website : https://mowangblog.top
 * @date : 2021/10/28 16:04
 **/
@Slf4j
@Component
public class WarningConsumer {
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveConfirmMessage(Message message){
        String msg = new String(message.getBody());
        log.info("报警发现不可路由消息:{}",msg);
    }
    @RabbitListener(queues = ConfirmConfig.BACKUP_QUEUE_NAME)
    public void receiveConfirmMessage2(Message message){
        String msg = new String(message.getBody());
        log.info("备份队列发现不可路由消息:{}",msg);
    }
}
