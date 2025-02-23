package sb.rabbitmqlogconsumer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sb.rabbitmqlogconsumer.config.queue.extention.DlqLogSettings;
import sb.rabbitmqlogconsumer.config.queue.extention.LogQueueSetting;
import sb.rabbitmqlogconsumer.config.queue.extention.RetryLogQueueSettings;

@Configuration
public class RabbitMQConfig {

//    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter messageConverter, ErrorHandler errorHandler) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(messageConverter);
//        factory.setErrorHandler(errorHandler); // Подключаем кастомный обработчик ошибок
//        return factory;
//    }
//
//    public DirectRabbitListenerContainerFactory directRabbitListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter messageConverter, ErrorHandler errorHandler) {
//        DirectRabbitListenerContainerFactory factory = new DirectRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(messageConverter);
//        factory.setErrorHandler(errorHandler); // Подключаем кастомный обработчик ошибок
//        return factory;
//    }
//
//    @Bean
//    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory, @Value("${spring.rabbitmq.listener.type}") String listenerType, MessageConverter messageConverter, ErrorHandler errorHandler) {
//        if (listenerType.equalsIgnoreCase("Direct")) {
//            return directRabbitListenerContainerFactory(connectionFactory, messageConverter, errorHandler);
//        } else {
//            return simpleRabbitListenerContainerFactory(connectionFactory, messageConverter, errorHandler);
//        }
//    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue mainQueue(LogQueueSetting logQueueSetting) {
        return QueueBuilder.durable(logQueueSetting.getName()).withArguments(logQueueSetting.getArguments()).build();
    }

    @Bean
    public Queue retryQueue(RetryLogQueueSettings retryQueueSetting) {
        return QueueBuilder.durable(retryQueueSetting.getName()).withArguments(retryQueueSetting.getArguments()).build();
    }

    @Bean
    public Queue dlq(DlqLogSettings dlqSetting) {
        return QueueBuilder.durable(dlqSetting.getName()).withArguments(dlqSetting.getArguments()).build();
    }

    @Bean
    public FanoutExchange mainLogExchange(LogQueueSetting logQueueSetting) {
        return new FanoutExchange(logQueueSetting.getExchange());
    }

    @Bean
    public FanoutExchange retryLogExchange(RetryLogQueueSettings retryQueueSetting) {
        return new FanoutExchange(retryQueueSetting.getExchange());
    }

    @Bean
    public FanoutExchange dlqExchange(DlqLogSettings dlqLogSettings) {
        return new FanoutExchange(dlqLogSettings.getExchange());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Binding bindingMainLogQueue(Queue mainQueue, FanoutExchange mainLogExchange) {
        return BindingBuilder.bind(mainQueue).to(mainLogExchange);
    }@Bean
    public Binding bindingRetryLogQueue(Queue retryQueue, FanoutExchange retryLogExchange) {
        return BindingBuilder.bind(retryQueue).to(retryLogExchange);
    }

    @Bean
    public Binding bindingDLQLog(Queue dlq, FanoutExchange dlqExchange) {
        return BindingBuilder.bind(dlq).to(dlqExchange);
    }
}
