package sb.rabbitmqlogconsumer.queue.produser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sb.rabbitmqlogconsumer.dto.LogRecordEvent;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper = new ObjectMapper();
    @Value("${spring.rabbitmq.queues.log-queue.exchange}")
    private String exchange;
    public void send(LogRecordEvent logRecordEvent) throws JsonProcessingException {
//        LogRecordEvent logR = objectMapper.readValue(logRecordEvent, LogRecordEvent.class);
        Message message = MessageBuilder.withBody(objectMapper.writeValueAsBytes(logRecordEvent))
                .setMessageId(UUID.randomUUID().toString())
                .build();
        rabbitTemplate.convertAndSend(exchange,"", message);
    }
}
