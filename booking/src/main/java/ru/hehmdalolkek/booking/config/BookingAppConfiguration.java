package ru.hehmdalolkek.booking.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class BookingAppConfiguration {

    public final static String BOOKING_EXCHANGE_NAME = "booking.direct";

    public final static String BOOKING_QUEUE_NAME = "booking.queue";

    public final static String NOTIFICATION_QUEUE_NAME = "notification.queue";

    public final static String PAYMENT_QUEUE_NAME = "payment.queue";

    public final static String NOTIFICATION_ROUTING_KEY = "booking.notification";

    public final static String PAYMENT_ROUTING_KEY = "booking.payment";

    @Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(BOOKING_EXCHANGE_NAME);
    }

    @Bean
    public Queue bookingQueue() {
        return new Queue(BOOKING_QUEUE_NAME);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE_NAME);
    }

    @Bean
    public Queue paymentQueue() {
        return new Queue(PAYMENT_QUEUE_NAME);
    }

    @Bean
    public Binding notificationBookingBinding() {
        return BindingBuilder.bind(notificationQueue()).to(bookingExchange()).with(NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public Binding paymentBookingBinding() {
        return BindingBuilder.bind(paymentQueue()).to(bookingExchange()).with(PAYMENT_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactory.setPackagesToScan("ru.hehmdalolkek.booking.model");
        return entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

}