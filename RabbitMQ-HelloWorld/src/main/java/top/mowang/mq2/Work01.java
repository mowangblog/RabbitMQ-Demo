package top.mowang.mq2;

import com.rabbitmq.client.Channel;
import top.mowang.util.RabbitMQUtil;

/**
 * RabbitMQ-Demo
 *  工作线程1
 * @author : Xuan Li
 * @website : https://mowangblog.top
 * @date : 2021/10/27 15:32
 **/
public class Work01 {
    /**
     * 消息队列名称
     * @author: Xuan Li
     * @date: 2021/10/27 14:49
     */
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

        //消费者消费消息
        //1消费哪个队列
        //2消费成功之后是否自动应答
        //3消费未成功消费的回调
        //4取消消费的回调
        System.out.println("工作线程01等待接收消息。。。。");
        channel.basicConsume(QUEUE_NAME, true, (str,message) -> {
            System.out.println("接收到的消息："+new String(message.getBody()));
        },s -> System.out.println(s+"取消消息消费接口回调逻辑"));
    }
}
