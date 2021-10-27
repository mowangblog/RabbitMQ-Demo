package top.mowang.mq1;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * RabbitMQ-Demo
 *  mq消费者
 * @author : Xuan Li
 * @website : https://mowangblog.top
 * @date : 2021/10/27 15:03
 **/
public class Consumer {
    /**
     * 消息队列名称
     * @author: Xuan Li
     * @date: 2021/10/27 14:49
     */
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //ip,连接mq的队列
        factory.setHost("192.168.64.128");
        factory.setUsername("admin");
        factory.setPassword("123");
        //创建连接
        Connection connection = factory.newConnection();
        //获得信道
        Channel channel = connection.createChannel();
        //消费者消费消息
        //1消费哪个队列
        //2消费成功之后是否自动应答
        //3消费未成功消费的回调
        //4取消消费的回调
        channel.basicConsume(QUEUE_NAME, true, (str,message) -> {
            System.out.println(str+"\t"+new String(message.getBody()));
        }, System.out::println);
    }
}
