package sb.rabbitmqlogconsumer.consumer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import sb.rabbitmqlogconsumer.dto.LogRecordEvent;
import sb.rabbitmqlogconsumer.repositories.EventRepository;


@Slf4j
@Component
@RequiredArgsConstructor
public class MessageConsumer {

    private final EventRepository<LogRecordEvent> eventRepository;

    @RabbitListener(queues = "${spring.rabbitmq.queues.log-queue.name}", errorHandler = "rabbitErrorHandler")
    private void receiveMessage(@Valid @Payload LogRecordEvent logRecordEvent) {
        System.out.println("message consume in receiveMessage: " + logRecordEvent);
        eventRepository.save(logRecordEvent);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.retry-log-queue.name}", errorHandler = "rabbitErrorHandler")
    private void receiveMessageRetry(@Valid @Payload LogRecordEvent logRecordEvent) {
        System.out.println("message consume in receiveMessageRetry" + logRecordEvent);
        eventRepository.save(logRecordEvent);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.dlq-log.name}")
    private void receiveMessageDlq(String message) {
        System.out.println("message consume in receiveMessageDlq" + message);
    }

}
