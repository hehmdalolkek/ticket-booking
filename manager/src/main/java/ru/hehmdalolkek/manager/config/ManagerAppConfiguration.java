package ru.hehmdalolkek.manager.config;

import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ManagerAppConfiguration {

    @Value("${rabbitmq.exchange.managerDirectExchange}")
    private String managerDirectExchangeName;

    @Value("${rabbitmq.queue.notificationQueue}")
    private String notificationQueueName;

    @Value("${rabbitmq.queue.bookingQueue}")
    private String bookingQueueName;

    @Value("${rabbitmq.routingKey.managerNotificationRoutingKey}")
    private String managerNotificationRoutingKeyName;

    @Value("${rabbitmq.routingKey.managerBookingRoutingKey}")
    private String managerBookingRoutingKeyName;

    @Bean
    Exchange managerExchange() {
        return new DirectExchange(this.managerDirectExchangeName);
    }

    @Bean
    Queue notificationQueue() {
        return new Queue(this.notificationQueueName);
    }

    @Bean
    Queue bookingQueue() {
        return new Queue(this.bookingQueueName);
    }

    @Bean
    Binding managerNotificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(managerExchange())
                .with(this.managerNotificationRoutingKeyName)
                .noargs();
    }

    @Bean
    Binding managerBookingBinding() {
        return BindingBuilder
                .bind(bookingQueue())
                .to(managerExchange())
                .with(this.managerBookingRoutingKeyName)
                .noargs();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
