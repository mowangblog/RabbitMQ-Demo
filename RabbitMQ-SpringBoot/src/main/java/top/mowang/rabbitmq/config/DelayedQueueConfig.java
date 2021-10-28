package top.mowang.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ-Demo
 * 基于插件的延迟队列配置
 *
 * @author : Xuan Li
 * @website : https://mowangblog.top
 * @date : 2021/10/28 14:55
 **/
@Configuration
@SuppressWarnings("all")
public class DelayedQueueConfig {
    //队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    //交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    //routingkey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    //自定义交换机
    @Bean
    public CustomExchange delayedExChange(){
        Map<String, Object> args = new HashMap<>(3);
        //声明交换机延迟类型
        args.put("x-delayed-type","direct");
        //1交换机名称，2交换机类型，3是否需要持久化，4，是否自动删除，5，其他参数
        return new CustomExchange(DELAYED_EXCHANGE_NAME,"x-delayed-message",
                true,false,args);
    }

    //延迟队列
    @Bean
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE_NAME);
    }

    //绑定队列到延迟交换机
    @Bean
    public Binding delayedQueueBindingDeayedExchange(@Qualifier("delayedQueue") Queue queue,
                                                     @Qualifier("delayedExChange") CustomExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
