package sb.rabbitmqlogconsumer.queue.consumer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import sb.rabbitmqlogconsumer.dto.ErrorMessage;
import sb.rabbitmqlogconsumer.dto.LogRecordEvent;
import sb.rabbitmqlogconsumer.queue.consumer.enums.Headers;
import sb.rabbitmqlogconsumer.repositories.EventRepository;

import java.time.LocalDateTime;


@Slf4j
@Component
@RequiredArgsConstructor
public class MessageConsumer {

    private final EventRepository<LogRecordEvent> eventRepository;
    private final EventRepository<ErrorMessage> errorMessageRepository;

    @RabbitListener(queues = "${spring.rabbitmq.queues.log-queue.name}", errorHandler = "rabbitErrorHandler")
    private void receiveMessage(@Valid @Payload LogRecordEvent logRecordEvent) {
        System.out.println("message consume in receiveMessage: " + logRecordEvent);
        if (logRecordEvent.getTable().contains("error")) {
            throw new RuntimeException("error error da da");
        }
        eventRepository.save(logRecordEvent);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.retry-log-queue.name}", errorHandler = "rabbitErrorHandler")
    private void receiveMessageRetry(/*@Valid @Payload*/ LogRecordEvent logRecordEvent) {
        System.out.println("message consume in receiveMessageRetry: " + logRecordEvent);
        if (logRecordEvent.getTable().contains("error")) {
            throw new RuntimeException("error error da da");
        }
        eventRepository.save(logRecordEvent);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.dlq-log.name}")
    private void receiveMessageDlq(Message errorMessage) {
        System.out.println("message consume in receiveMessageDlq: " + errorMessage);
        errorMessageRepository.save(concertToErrorMessage(errorMessage));
    }

    private ErrorMessage concertToErrorMessage(Message message) {
        LocalDateTime localDateTime = LocalDateTime.parse(message.getMessageProperties().getHeader(Headers.X_TIMESTAMP.name()));
        return ErrorMessage.builder()
                .originalMessage(new String(message.getBody()))
                .queue(message.getMessageProperties().getHeader(Headers.X_ORIGIN_QUEUE.name()))
                .errorType(message.getMessageProperties().getHeader(Headers.X_ERROR_TYPE.name()))
                .errorMessage(message.getMessageProperties().getHeader(Headers.X_ERROR_MESSAGE.name()))
                .messageId(message.getMessageProperties().getMessageId())
                .retries(message.getMessageProperties().getHeader(Headers.X_RETRY_COUNT.name()))
                .timestamp(localDateTime)
                .build();
    }

}
