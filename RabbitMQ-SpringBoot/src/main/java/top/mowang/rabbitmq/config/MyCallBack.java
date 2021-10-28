package top.mowang.rabbitmq.config;

import com.rabbitmq.client.ReturnCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;

/**
 * RabbitMQ-Demo
 *
 * @author : Xuan Li
 * @website : https://mowangblog.top
 * @date : 2021/10/28 16:10
 **/
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }
    /**
     * 交换机不管是否收到消息的一个回调方法
     * CorrelationData
     * 1消息相关数据
     * ack
     * 2交换机是否收到消息
     * 3失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        String id=correlationData!=null?correlationData.getId():"";
        if(b){
            log.info("交换机已经收到 id 为:{}的消息",id);
        }else{
            log.info("交换机还未收到 id 为:{}消息,由于原因:{}",id,s);
        }

    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error(" 消 息 {}, 被 交 换 机 {} 退 回 ， 退 回 原 因 :{}, 路 由 key:{}",new
                String(returnedMessage.getMessage().getBody()),
                returnedMessage.getExchange(),returnedMessage.getReplyText(),
                returnedMessage.getRoutingKey());
    }
}
