package ewm.event.controller;

import ewm.ParamHitDto;
import ewm.client.RestStatClient;
import ewm.event.dto.EventFullDto;
import ewm.event.dto.EventShortDto;
import ewm.event.dto.ReqParam;
import ewm.event.model.EventSort;
import ewm.event.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {
    private static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    private static final String MAIN_SERVICE = "ewm-main-service";
    private final EventService eventService;
    private final RestStatClient statClient;

    @GetMapping
    public List<EventShortDto> publicGetAllEvents(@RequestParam String text,
                                                  @RequestParam List<Long> categories,
                                                  @RequestParam Boolean paid,
                                                  @RequestParam @DateTimeFormat(pattern = FORMAT_DATETIME) LocalDateTime rangeStart,
                                                  @RequestParam @DateTimeFormat(pattern = FORMAT_DATETIME) LocalDateTime rangeEnd,
                                                  @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                  @RequestParam EventSort sort,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  HttpServletRequest request) {
        ReqParam reqParam = ReqParam.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build();
        List<EventShortDto> events = eventService.publicGetAllEvents(reqParam);
        hit(request);
        return events;
    }

    @GetMapping("/{id}")
    public EventFullDto publicGetEvent(@PathVariable long id,
                                       HttpServletRequest request) {
        EventFullDto eventFullDto = eventService.publicGetEvent(id);
        hit(request);
        return eventFullDto;
    }

    private void hit(HttpServletRequest request) {
        ParamHitDto endpointHitDto = new ParamHitDto();
        endpointHitDto.setApp(MAIN_SERVICE);
        endpointHitDto.setUri(request.getRequestURI());
        endpointHitDto.setIp(request.getRemoteAddr());
        endpointHitDto.setTimestamp(LocalDateTime.now());
        statClient.hit(endpointHitDto);
    }
}