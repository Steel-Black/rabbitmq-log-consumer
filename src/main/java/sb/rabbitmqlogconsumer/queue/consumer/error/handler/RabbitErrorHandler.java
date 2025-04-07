package sb.rabbitmqlogconsumer.queue.consumer.error.handler;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.stereotype.Component;
import sb.rabbitmqlogconsumer.config.settings.extention.DlqLogSettings;
import sb.rabbitmqlogconsumer.config.settings.extention.LogQueueSetting;
import sb.rabbitmqlogconsumer.config.settings.extention.RetryLogQueueSettings;
import sb.rabbitmqlogconsumer.queue.consumer.enums.Headers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RabbitErrorHandler implements RabbitListenerErrorHandler {
//    @Value("${spring.rabbitmq.listener.direct.retry.max-attempts}")
    @Value("${spring.rabbitmq.listener.simple.retry.max-attempts}")
    private int maxRetries;
    private final RabbitTemplate rabbitTemplate;
    private final RetryLogQueueSettings retryLogQueueSettings;
    private final DlqLogSettings dlqLogSettings;
    private final LogQueueSetting logQueueSetting;

    @Override
    public Object handleError(Message message, Channel channel, org.springframework.messaging.Message<?> message1, ListenerExecutionFailedException e) throws IOException {
        System.err.println("Exception: " + e.getCause().getMessage());
        // Проверяем тип ошибки
        Throwable cause = e.getCause();
        if (cause instanceof MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            System.out.println("errors " + errors);
            System.out.println("Sending message to DLQ...");
            rabbitTemplate.send(dlqLogSettings.getExchange(), "", message);
        }
        String qType = message.getMessageProperties().getConsumerQueue();
        if (qType.equals(logQueueSetting.getName())) {
            logQueueErrorProcess(message);
        } else if (qType.equals(retryLogQueueSettings.getName())) {
            retryLogQueueErrorProcess(message, e);
        } else if (qType.equals(dlqLogSettings.getName())) {
            dlqErrorProcess();
        }
        return null;
    }

    //TODO ЗАПИЛИТЬ РУЧНОЕ УПРАВЛЕНИЕ СООБЩЕНИЯМИ. В RETRY_QUEUE РЕАЛЗИОВАТЬ СЧЕТЧИК ПЕРЕДОСТАВКИ СООБЩЕНИЯ И ЛОГИКУ ПОВЕДЕНИЯ С ТАКИМИ СООБЩЕНИЯМИ.
    //TODO СДЕЛАТЬ ОБРАБОТКУ СООБЩЕНИЙ ОШИБОК С УСТАНОВКОЙ ИМ ТЕКСТА ОШИБКИ И ТИПА ОШИБКИ КАК ЗАГОЛОВОК СООБЩЕНИЯ
    private void dlqErrorProcess() {
    }

    private void retryLogQueueErrorProcess(Message message, ListenerExecutionFailedException e) throws IOException {
//        if (true){
//            return;
//        }
        int retries = message.getMessageProperties().getHeader(Headers.X_RETRY_COUNT.name());
        if (retries >= maxRetries) {
            message.getMessageProperties().setHeader(Headers.X_ERROR_TYPE.name(), e.getCause().getClass().getSimpleName());
            message.getMessageProperties().setHeader(Headers.X_ERROR_MESSAGE.name(), e.getCause());
            rabbitTemplate.send(dlqLogSettings.getExchange(), "", message);
        } else {
            message.getMessageProperties().setHeader(Headers.X_RETRY_COUNT.name(), ++retries);
            rabbitTemplate.send(retryLogQueueSettings.getExchange(), "", message);
        }
    }
    private void logQueueErrorProcess(Message message) throws IOException {
        System.out.println("Send message to retry queue...");
        message.getMessageProperties().setHeader(Headers.X_TIMESTAMP.name(), LocalDateTime.now());
        message.getMessageProperties().setHeader(Headers.X_RETRY_COUNT.name(), 1);
        message.getMessageProperties().setHeader(Headers.X_ORIGIN_QUEUE.name(), message.getMessageProperties().getConsumerQueue());
        rabbitTemplate.send(retryLogQueueSettings.getExchange(), "", message);
    }

    private void rejectNoRetry(Message message, Channel channel) throws IOException {
        channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
    }

    private void rejectRetry(Message message, Channel channel) throws IOException {
        channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
    }

}

