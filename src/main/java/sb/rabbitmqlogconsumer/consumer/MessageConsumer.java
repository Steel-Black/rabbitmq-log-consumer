package sb.rabbitmqlogconsumer.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class MessageConsumer {

    @RabbitListener(queues = "${spring.rabbitmq.queues.log-queue.name}", errorHandler = "rabbitErrorHandler")
    private void receiveMessage(String message) {
        System.out.println("message consume in receiveMessage" + message);

    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.retry-log-queue.name}", errorHandler = "rabbitErrorHandler")
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

    private void saveMessage(String message) {

    }
}
