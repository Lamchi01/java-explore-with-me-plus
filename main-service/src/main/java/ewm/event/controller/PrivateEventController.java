package ewm.event.controller;

import ewm.event.dto.NewEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
//@Validated
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
//    private final EventService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public NewEventDto create(@PathVariable(name = "userId") Long userId,
                              @RequestBody NewEventDto newEventDto) {
        return newEventDto;
    }
}
