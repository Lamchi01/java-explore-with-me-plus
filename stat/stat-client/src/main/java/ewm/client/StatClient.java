package ewm.client;

import ewm.ParamDto;
import ewm.ParamHitDto;
import ewm.StatDto;

public interface StatClient {
    void hit(ParamHitDto paramHitDto);

    //
    StatDto getStat(ParamDto paramDto);
}