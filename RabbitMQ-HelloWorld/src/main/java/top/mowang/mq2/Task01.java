package top.mowang.mq2;

import com.rabbitmq.client.Channel;
import top.mowang.util.RabbitMQUtil;

import java.util.Scanner;

/**
 * RabbitMQ-Demo
 * 生产者01
 *
 * @author : Xuan Li
 * @website : https://mowangblog.top
 * @date : 2021/10/27 15:41
 **/
public class Task01 {
    /**
     * 消息队列名称
     * @author: Xuan Li
     * @date: 2021/10/27 14:49
     */
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        //产生一个队列
        //1队列名称，2消息是否持久化，默认消息存储在内存中
        //3队列是否只供一个消费者，是否进行消息共享，默认不共享
        //4是否自动删除，最后一个消费者断开连接后 是否自动删除
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        //发消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            //生产者发送一个消息
            //1发送到那个路由
            //2路由的key值是哪个 本次是队列的名称
            //3其他参数信息 4消息体
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("消息发送完毕");
        }
    }
}
