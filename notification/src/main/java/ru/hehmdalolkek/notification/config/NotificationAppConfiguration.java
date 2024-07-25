package ru.hehmdalolkek.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationAppConfiguration {

    public final static String MANAGER_EXCHANGE_NAME = "manager.direct";

    public final static String BOOKING_EXCHANGE_NAME = "booking.direct";

    public final static String PAYMENT_EXCHANGE_NAME = "payment.direct";

    public final static String NOTIFICATION_QUEUE_NAME = "notification.queue";

    public final static String MANAGER_NOTIFICATION_ROUTING_KEY = "manager.notification";

    public final static String BOOKING_NOTIFICATION_ROUTING_KEY = "booking.notification";

    public final static String PAYMENT_NOTIFICATION_ROUTING_KEY = "payment.notification";

    @Bean
    Exchange managerExchange() {
        return new DirectExchange(MANAGER_EXCHANGE_NAME);
    }

    @Bean
    Exchange bookingExchange() {
        return new DirectExchange(BOOKING_EXCHANGE_NAME);
    }

    @Bean
    Exchange paymentExchange() {
        return new DirectExchange(PAYMENT_EXCHANGE_NAME);
    }

    @Bean
    Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE_NAME);
    }

    @Bean
    Binding managerNotificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(managerExchange())
                .with(MANAGER_NOTIFICATION_ROUTING_KEY)
                .noargs();
    }


    @Bean
    Binding bookingNotificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(bookingExchange())
                .with(BOOKING_NOTIFICATION_ROUTING_KEY)
                .noargs();
    }


    @Bean
    Binding paymentNotificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(paymentExchange())
                .with(PAYMENT_NOTIFICATION_ROUTING_KEY)
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
