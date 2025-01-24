package ewm.event.controller;

import ewm.event.dto.EventFullDto;
import ewm.event.dto.NewEventDto;
import ewm.event.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final EventService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EventFullDto create(@PathVariable(name = "userId") long userId,
                               @Valid @RequestBody NewEventDto newEventDto) {
        return service.create(userId, newEventDto);
    }
}
