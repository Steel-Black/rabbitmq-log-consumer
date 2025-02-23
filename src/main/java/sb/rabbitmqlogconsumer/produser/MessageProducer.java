package sb.rabbitmqlogconsumer.produser;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;
    @Value("${spring.rabbitmq.queues.log-queue.exchange}")
    private String exchange;
    public void send(String message) {
        rabbitTemplate.convertAndSend(exchange,"", message);
    }
}
