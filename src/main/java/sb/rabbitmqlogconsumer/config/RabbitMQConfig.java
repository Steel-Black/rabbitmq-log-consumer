package sb.rabbitmqlogconsumer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sb.rabbitmqlogconsumer.config.settings.extention.DlqLogSettings;
import sb.rabbitmqlogconsumer.config.settings.extention.LogQueueSetting;
import sb.rabbitmqlogconsumer.config.settings.extention.RetryLogQueueSettings;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
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
