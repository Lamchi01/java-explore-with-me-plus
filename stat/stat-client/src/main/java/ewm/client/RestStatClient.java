package ewm.client;

import ewm.ParamDto;
import ewm.ParamHitDto;
import ewm.ViewStats;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class RestStatClient implements StatClient {
    private final String statUrl;
    private final RestClient restClient;

    public RestStatClient(@Value("${client.url}") String statUrl) {
        this.statUrl = statUrl;
        this.restClient = RestClient.builder()
                .baseUrl(statUrl)
                .build();
    }

    @Override
    public void hit(ParamHitDto paramHitDto) {
        restClient.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(paramHitDto)
                .retrieve()
                .onStatus(status -> status != HttpStatus.CREATED, (request, response) -> {
                    throw new RuntimeException("Ошибка при создании.");
                });
    }

    @Override
    public List<ViewStats> getStat(ParamDto paramDto) {
        return restClient.get().uri(uriBuilder -> uriBuilder.path("/stats")
                        .queryParam("start", paramDto.getStart().toString())
                        .queryParam("end", paramDto.getEnd().toString())
                        .queryParam("uris", paramDto.getUris())
                        .queryParam("unique", paramDto.getUnique())
                        .build())
                .retrieve()
                .onStatus(status -> status != HttpStatus.OK, (request, response) -> {
                    throw new RuntimeException("Ошибка при создании.");
                })
                .body(ParameterizedTypeReference.forType(List.class));
    }

//    @Override
//    public ViewStats getStat(ParamDto paramDto) {
//        return restClient.get().uri(uriBuilder -> uriBuilder.path("/stats")
//                        .queryParam("start", URLEncoder.encode(localDateTimeFormatter(paramDto.getStart()), StandardCharsets.UTF_8))
//                        .queryParam("end", URLEncoder.encode(localDateTimeFormatter(paramDto.getEnd()), StandardCharsets.UTF_8))
//                        .queryParam("uris", paramDto.getUris())
//                        .queryParam("unique", paramDto.getUnique())
//                        .build())
//                .retrieve()
//                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
//                    throw new RuntimeException("Ошибка сервера.");
//                })
//                .body(ViewStats.class);
//    }
//
//    private String localDateTimeFormatter(LocalDateTime dateTime) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        return dateTime.format(formatter);
//    }
}