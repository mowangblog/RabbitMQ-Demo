package top.mowang.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * RabbitMQ-Demo
 *  死信队列消费者
 * @author : Xuan Li
 * @website : https://mowangblog.top
 * @date : 2021/10/28 11:46
 **/
@Slf4j
@Component
@SuppressWarnings("all")
public class DeadLetterConsumer {

    //接受消息
    @RabbitListener(queues = "QD")
    public void receviceD(Message message, Channel channel){
        String msg = new String(message.getBody());
        log.info("当前时间{}，收到死信队列消息：{}",new Date().toString(),msg);
    }


}
