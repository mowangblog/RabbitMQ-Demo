package top.mowang.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.mowang.rabbitmq.config.DelayedQueueConfig;

import java.util.Date;

/**
 * RabbitMQ-Demo
 *  基于插件的延迟消息
 * @author : Xuan Li
 * @website : https://mowangblog.top
 * @date : 2021/10/28 15:08
 **/
@Slf4j
@Component
@SuppressWarnings("all")
public class DelayedQueueConsumer {

    //监听接受消息
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receviceDelayedQueue(Message message){
        String msg = new String(message.getBody());
        log.info("当前时间{}，收到延迟队列队列消息：{}",new Date().toString(),msg);
    }
}
