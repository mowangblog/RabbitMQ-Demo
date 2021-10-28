package top.mowang.mq1;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ-Demo
 * mq生产者
 *
 * @author : Xuan Li
 * @website : https://mowangblog.top
 * @date : 2021/10/27 14:48
 **/
public class Producer {
    /**
     * 消息队列名称
     *
     * @author: Xuan Li
     * @date: 2021/10/27 14:49
     */
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
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
        //产生一个队列
        //1队列名称，2消息是否持久化，默认消息存储在内存中
        //3队列是否只供一个消费者，是否进行消息共享，默认不共享
        //4是否自动删除，最后一个消费者断开连接后 是否自动删除
        Map<String, Object> params = new HashMap();
        params.put("x-max-priority", 10);
        channel.queueDeclare(QUEUE_NAME, true, false, false, params);
        //发消息
        //生产者发送一个消息
        //1发送到那个路由
        //2路由的key值是哪个 本次是队列的名称
        //3其他参数信息 4消息体
        for (int i = 1; i < 11; i++) {
            String message = "hello" + i;
            if (i == 8) {
                AMQP.BasicProperties properties = new
                        AMQP.BasicProperties().builder().priority(8).build();
                channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
            }else {
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            }

        }
        System.out.println("消息发送完毕");
    }
}
