package ru.hehmdalolkek.booking.config;

import jakarta.persistence.EntityManagerFactory;
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
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Getter
@Configuration
public class BookingAppConfiguration {

    @Value("${rabbitmq.exchange.bookingDirectExchange}")
    private String bookingDirectExchangeName;

    @Value("${rabbitmq.queue.bookingQueue}")
    private String bookingQueueName;

    @Value("${rabbitmq.queue.notificationQueue}")
    private String notificationQueueName;

    @Value("${rabbitmq.queue.paymentQueue}")
    private String paymentQueueName;

    @Value("${rabbitmq.routingKey.bookingNotificationRoutingKey}")
    private String bookingNotificationRoutingKey;

    @Value("${rabbitmq.routingKey.bookingPaymentRoutingKey}")
    private String bookingPaymentRoutingKey;

    @Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(this.bookingDirectExchangeName);
    }

    @Bean
    public Queue bookingQueue() {
        return new Queue(this.bookingQueueName);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(this.notificationQueueName);
    }

    @Bean
    public Queue paymentQueue() {
        return new Queue(this.paymentQueueName);
    }

    @Bean
    public Binding notificationBookingBinding() {
        return BindingBuilder.bind(notificationQueue()).to(bookingExchange()).with(this.bookingNotificationRoutingKey);
    }

    @Bean
    public Binding paymentBookingBinding() {
        return BindingBuilder.bind(paymentQueue()).to(bookingExchange()).with(this.bookingPaymentRoutingKey);
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