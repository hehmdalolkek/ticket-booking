package ru.hehmdalolkek.payment.config;

import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class PaymentAppConfiguration {

    @Value("${rabbitmq.exchange.paymentDirectExchange}")
    private String paymentDirectExchangeName;

    @Value("${rabbitmq.queue.paymentQueue}")
    private String paymentQueueName;

    @Value("${rabbitmq.queue.notificationQueue}")
    private String notificationQueueName;

    @Value("${rabbitmq.routingKey.paymentNotificationRoutingKey}")
    private String paymentNotificationRoutingKeyName;

    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(this.paymentDirectExchangeName);
    }

    @Bean
    public Queue paymentQueue() {
        return new Queue(this.paymentQueueName);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(this.notificationQueueName);
    }

    @Bean
    public Binding paymentNotificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(paymentExchange())
                .with(this.paymentNotificationRoutingKeyName);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
