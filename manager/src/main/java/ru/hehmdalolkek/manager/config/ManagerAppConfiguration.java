package ru.hehmdalolkek.manager.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManagerAppConfiguration {

    public final static String MANAGER_EXCHANGE_NAME = "manager.direct";

    public final static String NOTIFICATION_QUEUE_NAME = "manager.notification";

    public final static String BOOKING_QUEUE_NAME = "manager.booking";

    public final static String NOTIFICATION_ROUTING_KEY = "manager.notification";

    public final static String BOOKING_ROUTING_KEY = "manager.booking";

    @Bean
    Exchange managerExchange() {
        return new DirectExchange(MANAGER_EXCHANGE_NAME);
    }

    @Bean
    Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE_NAME);
    }

    @Bean
    Queue bookingQueue() {
        return new Queue(BOOKING_QUEUE_NAME);
    }

    @Bean
    Binding managerNotificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(managerExchange())
                .with(NOTIFICATION_ROUTING_KEY)
                .noargs();
    }

    @Bean
    Binding managerBookingBinding() {
        return BindingBuilder
                .bind(bookingQueue())
                .to(managerExchange())
                .with(BOOKING_ROUTING_KEY)
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
