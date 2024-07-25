package ru.hehmdalolkek.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.hehmdalolkek.basemodels.dto.NotificationDto;
import ru.hehmdalolkek.notification.config.NotificationAppConfiguration;
import ru.hehmdalolkek.notification.mapper.NotificationMapper;
import ru.hehmdalolkek.notification.model.Notification;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMapper notificationMapper;

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String username;

    @RabbitListener(queues = NotificationAppConfiguration.NOTIFICATION_QUEUE_NAME)
    public void receiveNotification(NotificationDto notificationDto) {
        log.info("Received notification: {}", notificationDto);
        Notification notification = this.notificationMapper.NotificationDtoToNotification(notificationDto);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(notification.getReservation().getEmail());
        simpleMailMessage.setText(notification.getMessage());
        simpleMailMessage.setSubject(
                "Ticket Booking Notification | %s".formatted(
                        notification.isError() ? "ERROR" : notification.getStatus()));

        this.emailSender.send(simpleMailMessage);
    }

}

