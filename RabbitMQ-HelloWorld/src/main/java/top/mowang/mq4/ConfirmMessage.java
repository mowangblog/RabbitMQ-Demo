package top.mowang.mq4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import top.mowang.util.RabbitMQUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * RabbitMQ-Demo
 * 发布确认模式
 * 1,单个确认
 * 2,批量确认
 * 3,异步确认
 *
 * @author : Xuan Li
 * @website : https://mowangblog.top
 * @date : 2021/10/27 18:20
 **/
public class ConfirmMessage {

    private static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
//         1,单个确认
//        ConfirmMessage.confirmSingle();
//         2,批量确认
//          ConfirmMessage.confirmBatch();
//         3,异步确认
          confirmSync();
    }

    public static void confirmSingle() throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        //1队列名称，2消息是否持久化，默认消息存储在内存中
        //3队列是否只供一个消费者，是否进行消息共享，默认不共享
        //4是否自动删除，最后一个消费者断开连接后 是否自动删除
        channel.queueDeclare(queueName, true, false, false, null);
        //开启确认发布
        channel.confirmSelect();
        //开始时间
        long start = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "i=" + i;
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送完毕" + message);
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("1,单个确认模式消耗时间=" + (end - start));
    }

    public static void confirmBatch() throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        //1队列名称，2消息是否持久化，默认消息存储在内存中
        //3队列是否只供一个消费者，是否进行消息共享，默认不共享
        //4是否自动删除，最后一个消费者断开连接后 是否自动删除
        channel.queueDeclare(queueName, true, false, false, null);
        //开启确认发布
        channel.confirmSelect();
        //批量确认消息大小
        int batchSize = 100;
        //开始时间
        long start = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            //批量发布，批量确认
            String message = "i=" + i;
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            if (i % batchSize == 0) {
                channel.waitForConfirms();
            }
            System.out.println("消息发送完毕" + message);
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("2,批量确认模式消耗时间=" + (end - start));
    }

    public static void confirmSync() throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        //1队列名称，2消息是否持久化，默认消息存储在内存中
        //3队列是否只供一个消费者，是否进行消息共享，默认不共享
        //4是否自动删除，最后一个消费者断开连接后 是否自动删除
        channel.queueDeclare(queueName, true, false, false, null);
        //开启确认发布
        channel.confirmSelect();

        //线程安全有序的哈希表，适用于高并发的情况下
        //轻松将序号和消息进行关联
        //轻松的批量删除 只要给序号
        //支持高并发（多线程）
        ConcurrentSkipListMap<Long, String> confirmListMap = new ConcurrentSkipListMap<>();

        //准备消息监听器
        //1成功的2失败的
        channel.addConfirmListener(new ConfirmCallback() {
            @Override
            public void handle(long l, boolean b) throws IOException {
                //删除已确认的消息 b是否批量
                if(b){
                    ConcurrentNavigableMap<Long, String> headMap = confirmListMap.headMap(l);
                    headMap.clear();
                }else {
                    confirmListMap.remove(l);
                }
                //成功的
                System.out.println("确认的消息："+l);

            }
        }, new ConfirmCallback() {
            @Override
            public void handle(long l, boolean b) throws IOException {
                String message = confirmListMap.get(l);
                //失败的
                System.out.println("未确认的消息："+message);
            }
        });
        //开始时间
        long start = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            //批量发布
            String message = "i=" + i;
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            //记录所有发送的消息
            confirmListMap.put(channel.getNextPublishSeqNo(),message);
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("3,异步确认模式消耗时间=" + (end - start));
    }
}
