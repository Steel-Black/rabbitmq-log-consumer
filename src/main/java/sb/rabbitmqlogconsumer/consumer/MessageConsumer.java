package sb.rabbitmqlogconsumer.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageConsumer {

    @RabbitListener(queues = "${spring.rabbitmq.queues.log-queue.name}", errorHandler = "rabbitErrorHandler")
    private void receiveMessage(String message) {
        System.out.println("message consume in receiveMessage" + message);
        if (message.contains("error")) {

            throw new RuntimeException("error");
        }
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.retry-log-queue.name}")
    private void receiveMessageRetry(String message) {
        System.out.println("message consume in receiveMessageRetry" + message);

        if (message.contains("error")) {
            // Отклонение сообщения (не возвращать в очередь)
            throw new RuntimeException("error");
        }
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.dlq-log.name}")
    private void receiveMessageDlq(String message) {
        System.out.println("message consume in receiveMessageDlq" + message);
    }
}
