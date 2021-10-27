package top.mowang.mq8;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.mowang.util.RabbitMQUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ-Demo
 * 死信
 *
 * @author : Xuan Li
 * @website : https://mowangblog.top
 * @date : 2021/10/27 23:24
 **/
@SuppressWarnings("all")
public class Consumer03 {
    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        //声明死信队列
        String deadQueue = "dead-queue";
        System.out.println("等待接收消息........... ");
        DeliverCallback deliverCallback = (consumerTag, delivery) ->
        {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Consumer01 接收到消息" + message);
        };
        channel.basicConsume(deadQueue, true, deliverCallback, consumerTag -> {
        });
    }
}

