package ewm.event.controller;


import ewm.event.dto.AdminEventParams;
import ewm.event.dto.EventFullDto;
import ewm.event.model.EventState;
import ewm.event.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static ewm.utility.Constants.FORMAT_DATETIME;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Validated
public class AdminEventController {

    private final EventService eventService;


    @GetMapping
    public List<EventFullDto> adminGetAllEvents(
            @RequestParam List<Long> users,
            @RequestParam List<String> states,
            @RequestParam List<Long> categories,
            @RequestParam @DateTimeFormat(pattern = FORMAT_DATETIME) LocalDateTime rangeStart,
            @RequestParam @DateTimeFormat(pattern = FORMAT_DATETIME) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Set<EventState> stateSet = EventState.from(states);
        if (stateSet == null || stateSet.isEmpty()) {
            throw new IllegalArgumentException("Некорректные значения states: " + states);
        }

        AdminEventParams adminEventParams = new AdminEventParams(users, states, categories, rangeStart, rangeEnd, from, size);

        return eventService.publicGetAllEvents(adminEventParams);
    }


}
