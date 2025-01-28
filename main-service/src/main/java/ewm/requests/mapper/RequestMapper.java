package ewm.requests.mapper;

import ewm.requests.dto.ParticipationRequestDto;
import ewm.requests.model.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    @Mapping(target = "created", source = "createdOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ParticipationRequestDto toParticipationRequestDto(Request request);
}