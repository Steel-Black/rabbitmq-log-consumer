package sb.rabbitmqlogconsumer.produser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sb.rabbitmqlogconsumer.dto.LogRecordEvent;

@Component
@RequiredArgsConstructor
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper = new ObjectMapper();
    @Value("${spring.rabbitmq.queues.log-queue.exchange}")
    private String exchange;
    public void send(LogRecordEvent logRecordEvent) throws JsonProcessingException {
//        LogRecordEvent logR = objectMapper.readValue(logRecordEvent, LogRecordEvent.class);
        rabbitTemplate.convertAndSend(exchange,"", logRecordEvent);
    }
}
