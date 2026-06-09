---
title: RabbitMQ使用中问题的解决方案
date: 2023-05-06 10:37:22
tags:
- RabbitMQ使用中问题的解决方案
categories:
- 解决方案
---

# 使用RabbitMQ实现订单的超时取消

## 实现功能

1. 订单超时取消
2. 订单支付，删除延迟队列消息

## 流程

1. **当用户下单时，订单系统创建一个订单，并将订单信息、队列名称、消息ID保存到数据库中。**同时，订单系统将一个定时消息发送到延迟队列中，设置该消息的延迟时间为订单超时的时间。
2. 在延迟队列中，该定时消息被保存在一个指定的队列中，并等待一定的时间后触发。
3. **当订单超时时，延迟队列会将该定时消息从队列中取出，并将该消息发送到目标队列中。**
4. **订单系统的监听程序从目标队列中获取到该消息后，根据订单的状态判断该订单是否需要被取消。如果订单的状态为未支付，则将该订单设置为取消状态，并更新数据库中的订单信息。**如果订单的状态已经被更新为其他状态，例如已经支付，则不需要执行取消操作。
5. 订单系统将取消操作的结果返回给用户，并根据具体情况进行退款等操作。
6. **如果在订单定时消息还未被触发前，订单被支付了，消息队列应该取消该订单的定时消息。**这可以通过在订单支付时检查订单对应的定时消息是否存在来实现。**如果存在该定时消息，则可以将其从延迟队列中删除。**

```java
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/*
	假设有一个名为"delayed-orders-exchange"的交换机和一个名为"delayed-orders-queue"的延迟队列
*/
@Component
public class OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    public void cancelDelayedOrder(String orderId) {
        // 构造消息体
        byte[] messageBody = orderId.getBytes();

        // 构造消息属性
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-delay", 10000); // 延迟10秒
        Message message = new Message(messageBody, headers);

        // 发送消息到延迟队列
        rabbitTemplate.convertAndSend("delayed-orders-exchange", "delayed-orders-routing-key", message);

        // 获取消息ID（即唯一标识符）
        Object messageId = rabbitTemplate.getMessageConverter().fromMessage(message).getMessageProperties().getMessageId();

        // 在延迟队列中删除该消息
        Queue queue = new Queue("delayed-orders-queue");
        amqpAdmin.purgeQueue(queue.getName(), false); // 注意这里是将队列中所有消息删除，如果需要删除指定消息需要通过消息ID进行过滤
    }
}

```

在上面的代码中，我们使用RabbitTemplate向延迟队列发送了一条延迟10秒的消息，并获取了该消息的唯一标识符。然后，我们使用AmqpAdmin来删除延迟队列中的所有消息。**如果需要删除指定的消息，可以在purgeQueue方法中设置第二个参数为true，并使用withMessageId方法来指定要删除的消息ID。**

需要注意的是，上述代码中的删除操作将删除延迟队列中所有的消息，如果需要删除特定的消息，需要使用过滤器和消息ID来实现。另外，在使用延迟队列时，还需要考虑消息在队列中的存储时间和队列容量等问题，以免出现消息堆积等问题。





**在消费者中，也是可以获取到消息的id**，获取id的目的是在消息发送之前取消消息，那么在消息没有发送之前，消费者是不能获取到消息的id的，所以**这种方式是不能在延迟队列订单支付时删除消息使用的。**

在Spring AMQP中，当消费者通过SimpleMessageListenerContainer或MessageListenerAdapter监听队列时，可以通过Message对象的getMessageProperties方法获取消息的属性，包括消息ID。例如：

```java
javaCopy code@Component
public class MyMessageListener {

    @RabbitListener(queues = "my-queue")
    public void onMessage(Message message) {
        String messageId = message.getMessageProperties().getMessageId();
        // 处理消息
    }

}
```

​	

# RabbitMQ避免重复消费

**幂等性设计**：

- **原理**：确保消费者在处理每个消息时，操作是幂等的，即无论同一消息被处理多少次，最终结果是相同的。
- **实现**：在处理消息之前，检查消息是否已经处理过。例如，可以使用数据库记录每个消息的唯一ID，如果消息已经存在于数据库中，则跳过处理。

**消息去重**：

- **原理**：在消费者端对消息进行去重。
- **实现**：使用分布式缓存（如Redis）或数据库存储处理过的消息ID，在处理新消息之前先检查消息ID是否已存在。

```java
import com.rabbitmq.client.*;
import redis.clients.jedis.Jedis;

public class IdempotentConsumer {

    private static final String QUEUE_NAME = "your_queue_name";
    private static final String REDIS_HOST = "localhost";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();
             Jedis jedis = new Jedis(REDIS_HOST)) {

            boolean autoAck = false;
            channel.basicConsume(QUEUE_NAME, autoAck, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String messageId = properties.getMessageId();
                    if (messageId == null) {
                        // 如果消息没有ID，直接确认并丢弃
                        channel.basicAck(envelope.getDeliveryTag(), false);
                        return;
                    }

                    if (jedis.sismember("processed_message_ids", messageId)) {
                        // 如果消息已经处理过，直接确认
                        channel.basicAck(envelope.getDeliveryTag(), false);
                        return;
                    }

                    String message = new String(body, "UTF-8");
                    try {
                        // 处理消息
                        processMessage(message);

                        // 标记消息已处理
                        jedis.sadd("processed_message_ids", messageId);

                        // 处理成功，发送ACK
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    } catch (Exception e) {
                        // 处理失败，发送NACK并重新入队
                        channel.basicNack(envelope.getDeliveryTag(), false, true);
                    }
                }
            });

            System.out.println("Waiting for messages. To exit press CTRL+C");
            Thread.sleep(Long.MAX_VALUE);
        }
    }

    private static void processMessage(String message) {
        // 这里写消息处理的业务逻辑
        System.out.println("Processing message: " + message);
    }
}

```

# RabbitMQ消息积压怎么办

**增加消费者**：

- **水平扩展**：增加消费者的数量可以提高处理速度，从而减少消息积压。确保消费者的处理能力与生产者的消息生产速度匹配。

**优化消费者性能**：

- **并行处理**：通过多线程或异步处理来提高消费者的处理能力。
- **提升资源**：分配更多的 CPU 和内存资源给消费者，以提升处理速度。

**调整队列的配置**：

- **队列持久化**：设置队列为持久化状态，可以减少因为重启等原因导致的消息丢失。
- **消息持久化**：将消息标记为持久化（即使 RabbitMQ 重启，也不会丢失消息）。

**调整消息 TTL（生存时间）和死信队列**：

- **TTL（Time-To-Live）**：设置消息的生存时间，当消息在队列中存在超过 TTL 时间后，它会被自动删除。
- **死信队列（DLQ）**：将无法处理的消息转发到死信队列，以便进行后续处理或排查。