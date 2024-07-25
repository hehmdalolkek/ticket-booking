package ru.hehmdalolkek.notification.mapper;

import org.mapstruct.Mapper;
import ru.hehmdalolkek.basemodels.dto.NotificationDto;
import ru.hehmdalolkek.notification.model.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDto NotificationToNotificationDto(Notification reservation);

    Notification NotificationDtoToNotification(NotificationDto reservationDto);

}
