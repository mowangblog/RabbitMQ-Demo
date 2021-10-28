package top.mowang.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ-Demo
 * 延迟队列配置
 *
 * @author : Xuan Li
 * @website : https://mowangblog.top
 * @date : 2021/10/28 11:23
 **/
@Configuration
@SuppressWarnings("all")
public class TelQueueConfig {
    //普通交换机
    public static final String X_EXCHANGE = "X";
    //队列a
    public static final String QUEUE_A = "QA";
    //队列b
    public static final String QUEUE_B = "QB";
    //死信交换机
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //死信队列
    public static final String DEAD_LETTER_QUEUE = "QD";
    //普通队列qc
    public static final String QUEUE_C = "QC";

    //声明为普通队列c
    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> args = new HashMap<>(3);
        //声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //声明当前队列的死信路由 key
        args.put("x-dead-letter-routing-key", "YD");
        //声明队列的 TTL
//        args.put("x-message-ttl", 40000);
        return QueueBuilder.durable(QUEUE_C).withArguments(args).build();
    }

    //声明x交换机
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    //声明死信y交换机
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //声明为普通队列a ttl 为 10s
    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> args = new HashMap<>(3);
        //声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //声明当前队列的死信路由 key
        args.put("x-dead-letter-routing-key", "YD");
        //声明队列的 TTL
        args.put("x-message-ttl", 10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(args).build();
    }

    //声明为普通队列b ttl 为 40s
    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> args = new HashMap<>(3);
        //声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //声明当前队列的死信路由 key
        args.put("x-dead-letter-routing-key", "YD");
        //声明队列的 TTL
        args.put("x-message-ttl", 40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(args).build();
    }

    //死信队列
    @Bean("queueD")
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    //绑定队列A到交换机X
    @Bean
    public Binding queueABindX(@Qualifier("queueA") Queue queueA,
                               @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    //绑定队列B到交换机X
    @Bean
    public Binding queueBBindX(@Qualifier("queueB") Queue queueB,
                               @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    //绑定队列C到交换机X
    @Bean
    public Binding queueCBindX(@Qualifier("queueC") Queue queueC,
                               @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }

    //绑定队列D到交换机Y
    @Bean
    public Binding queueDBindY(@Qualifier("queueD") Queue queueD,
                               @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }
}
