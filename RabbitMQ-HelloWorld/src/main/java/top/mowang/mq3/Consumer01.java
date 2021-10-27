package top.mowang.mq3;

import com.rabbitmq.client.Channel;
import top.mowang.util.RabbitMQUtil;

/**
 * RabbitMQ-Demo
 * 手动应答消费者
 *
 * @author : Xuan Li
 * @website : https://mowangblog.top
 * @date : 2021/10/27 17:28
 **/
public class Consumer01 {
    /**
     * 消息队列名称
     * @author: Xuan Li
     * @date: 2021/10/27 14:49
     */
    private static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        //设置不公平分发
        channel.basicQos(5);
        //消费者消费消息
        //1消费哪个队列
        //2消费成功之后是否自动应答
        //3消费未成功消费的回调
        //4取消消费的回调
        System.out.println("消费者01等待接收消息（处理时间较快）。。。。");
        channel.basicConsume(QUEUE_NAME, false, (str,message) -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("接收到的消息："+new String(message.getBody()));
            //1消息的标记
            //2是否批量应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        },s -> System.out.println(s+"取消消息消费接口回调逻辑"));
    }
}
