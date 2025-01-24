package ewm.event.mapper;

import ewm.event.dto.EventFullDto;
import ewm.event.dto.EventShortDto;
import ewm.event.dto.NewEventDto;
import ewm.event.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    @Mapping(target = "views", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    List<EventShortDto> toEventShortDto(List<Event> event);

    @Mapping(target = "views", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    EventFullDto toEventFullDto(Event event);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "eventDate", source = "eventDate")
    Event toEvent(NewEventDto newEventDto);

    default LocalDateTime stringToLocalDateTime(String stringDate) {
        if (stringDate == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATETIME);
        return LocalDateTime.parse(stringDate, formatter);
    }

    default String localDateTimeToString(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATETIME);
        return localDateTime.format(formatter);
    }
}
