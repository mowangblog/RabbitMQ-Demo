package top.mowang.mq7;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.mowang.util.RabbitMQUtil;

import java.util.Map;

/**
 * RabbitMQ-Demo
 *
 * @author : Xuan Li
 * @website : https://mowangblog.top
 * @date : 2021/10/27 21:45
 **/
public class ReceiveLogsTopic01 {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明队列
        String queueName = "Q1";
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName,EXCHANGE_NAME,"*.orange.*");
        System.out.println("等待接收消息,把接收到的消息打印在屏幕........... ");
        DeliverCallback deliverCallback = (consumerTag, delivery) ->
        {String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("ReceiveLogsTopic01控制台打印接收到的消息"+message+"绑定:"+delivery.getEnvelope().getRoutingKey());
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
