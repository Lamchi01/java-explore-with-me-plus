package ewm.event.service;

import ewm.event.dto.EventFullDto;
import ewm.event.dto.EventShortDto;
import ewm.event.dto.ReqParam;

import java.util.List;

public interface EventService {

    List<EventShortDto> publicGetAllEvents(ReqParam reqParam);

    EventFullDto publicGetEvent(long id);
}