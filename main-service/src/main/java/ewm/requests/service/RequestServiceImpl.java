package ewm.requests.service;

import ewm.event.model.Event;
import ewm.event.model.EventState;
import ewm.event.repository.EventRepository;
import ewm.exception.*;
import ewm.requests.dto.ParticipationRequestDto;
import ewm.requests.mapper.RequestMapper;
import ewm.requests.model.Request;
import ewm.requests.model.RequestStatus;
import ewm.requests.repository.RequestRepository;
import ewm.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        List<Request> requests = requestRepository.findByRequesterId(userId);
        return requests.stream()
                .map(requestMapper::toParticipationRequestDto)
                .toList();
    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        if (eventRepository.findByIdAndInitiatorId(eventId, userId).isPresent()) {
            throw new InitiatorRequestException("Пользователь с ID - " + userId + ", не найден.");
        }

        if (!requestRepository.findByRequesterIdAndEventId(userId, eventId).isEmpty()) {
            throw new RepeatUserRequestorException("Пользователь с ID - " + userId + ", не найден.");
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, "Событие с ID - " + eventId + ", не найдено."));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotPublishEventException("Данное событие ещё не опубликовано");
        }

        Request request = new Request();
        request.setRequester(userRepository.findById(userId).get());
        request.setEvent(event);

        Long confirmedRequests = requestRepository.countRequestsByEventAndStatus(event, RequestStatus.CONFIRMED);
        if (confirmedRequests >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ParticipantLimitException("Достигнут лимит участников для данного события.");
        }

        if (event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
            request.setCreatedOn(LocalDateTime.now());
            return requestMapper.toParticipationRequestDto(requestRepository.save(request));
        }

        if (event.getRequestModeration()) {
            request.setStatus(RequestStatus.PENDING);
            request.setCreatedOn(LocalDateTime.now());
            return requestMapper.toParticipationRequestDto(requestRepository.save(request));
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
            request.setCreatedOn(LocalDateTime.now());
        }
        return requestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        Request cancelRequest = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new EntityNotFoundException(Request.class, "Запрос с ID - " + requestId + ", не найден."));
        cancelRequest.setStatus(RequestStatus.CANCELED);
        return requestMapper.toParticipationRequestDto(requestRepository.save(cancelRequest));
    }
}