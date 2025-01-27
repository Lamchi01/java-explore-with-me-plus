package ewm.compilation.controller;

import ewm.compilation.dto.CompilationDto;
import ewm.compilation.service.CompilationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Validated
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public Collection<CompilationDto> getCompilations(@RequestParam(name = "pinned", defaultValue = "false") Boolean pinned,
                                                      @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                      @RequestParam(name = "size", defaultValue = "0") Integer size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable(name = "compId") Long compId) {
        return compilationService.getCompilationById(compId);
    }
}
