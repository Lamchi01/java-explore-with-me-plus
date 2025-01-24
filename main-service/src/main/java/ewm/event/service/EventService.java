package ewm.event.service;

import ewm.event.dto.EventFullDto;
import ewm.event.dto.EventShortDto;
import ewm.event.dto.NewEventDto;
import ewm.event.dto.ReqParam;
import ewm.event.model.Event;

import java.util.List;

public interface EventService {

    List<EventShortDto> publicGetAllEvents(ReqParam reqParam);

    EventFullDto publicGetEvent(long id);

    EventFullDto create(long userId, NewEventDto newEventDto);

}