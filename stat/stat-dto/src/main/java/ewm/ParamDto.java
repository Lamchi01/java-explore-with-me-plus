package ewm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@StartBeforeEnd
public class ParamDto implements StartEnd{

    private LocalDateTime start;

    private LocalDateTime end;

    private List<String> uris;

    private Boolean unique;
}
