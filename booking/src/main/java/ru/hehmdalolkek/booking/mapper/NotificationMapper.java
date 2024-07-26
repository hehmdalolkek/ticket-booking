package ru.hehmdalolkek.booking.mapper;

import org.mapstruct.Mapper;
import ru.hehmdalolkek.basemodels.dto.NotificationDto;
import ru.hehmdalolkek.booking.model.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDto NotificationToNotificationDto(Notification reservation);

    Notification NotificationDtoToNotification(NotificationDto reservationDto);

}
