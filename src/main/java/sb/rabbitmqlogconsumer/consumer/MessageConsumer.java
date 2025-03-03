package sb.rabbitmqlogconsumer.consumer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import sb.rabbitmqlogconsumer.dto.LogRecordEvent;
import sb.rabbitmqlogconsumer.repositories.EventRepository;


@Component
@RequiredArgsConstructor
public class MessageConsumer {

    private final EventRepository<LogRecordEvent> eventRepository;

    @RabbitListener(queues = "${spring.rabbitmq.queues.log-queue.name}", errorHandler = "rabbitErrorHandler")
    private void receiveMessage(LogRecordEvent logRecordEvent) {
        System.out.println("message consume in receiveMessage" + logRecordEvent);
        eventRepository.save(logRecordEvent);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.retry-log-queue.name}", errorHandler = "rabbitErrorHandler")
    private void receiveMessageRetry(@Valid LogRecordEvent logRecordEvent) {
        System.out.println("message consume in receiveMessageRetry" + logRecordEvent);

    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.dlq-log.name}")
    private void receiveMessageDlq(String message) {
        System.out.println("message consume in receiveMessageDlq" + message);
    }

}
