package ewm.client;

import ewm.ParamDto;
import ewm.ParamHitDto;
import ewm.StatDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RestStatClient implements StatClient {
    //    final RestTemplate template;
    private final String statUrl;
    private final RestClient restClient;

//    public RestStatClient(RestTemplate template, @Value("${client.url}") String statUrl) {
//        this.template = template;
//        this.statUrl = statUrl;
//    }

    public RestStatClient(@Value("${client.url}") String statUrl) {
        this.statUrl = statUrl;
        this.restClient = RestClient.builder()
                .baseUrl(statUrl)
                .build();
    }

//    public RestStatClient(@Value("http://localhost:9090") String statUrl) {
//        this.statUrl = statUrl;
//        this.restClient = RestClient.builder()
//                .baseUrl(statUrl)
//                .build();
//    }


    @Override
    public void hit(ParamHitDto paramHitDto) {
        restClient.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(paramHitDto)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Ошибка сервера.");
                })
                .onStatus(status -> status != HttpStatus.CREATED, (request, response) -> {
                    throw new RuntimeException("Ошибка при создании.");
                });
    }

    //
    @Override
    public StatDto getStat(ParamDto paramDto) {
        return restClient.get().uri(uriBuilder -> uriBuilder.path("/stats")
                        .queryParam("start", URLEncoder.encode(localDateTimeFormatter(LocalDateTime.now()), StandardCharsets.UTF_8))
                        .queryParam("end", URLEncoder.encode(localDateTimeFormatter(LocalDateTime.now()), StandardCharsets.UTF_8))
                        .queryParam("uris", paramDto)
                        .queryParam("unique", paramDto)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Ошибка сервера.");
                })
                .body(StatDto.class);
    }

    private String localDateTimeFormatter(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    /*public static void main(String[] args) {

//		String uri = "kjkj kjkf 8#$#$#$^ 65^%#@#$%";
//		statClient.hit(Collections.singltonList(uri));
//
//		for (String uri : uris) {
//			uriBuilder.append("&uris=").append(uri);
//		}
//
//		String join = String.join(",", uris);
//
//		// stat/hit?uris=/events/1,/events/2,/events/3 - не работает
//		Sting uris;
//		// stat/hit?uris=/events/1&uris=/events/2&uris=/events/3 - работает
        List<String> uris = new ArrayList<>();
//		String url = "stat/hit?uris=" + uris; // Так не правильно
        // немного лучше
        String url = "stat/hit?"; // Так не правильно
        for (String uri : uris) {
            url = url + "&uris=" + uri;
        }
        // Лучше через Uri builder


//        GET /events/{eventId}
// 1. получили данные от репозитория
// 2. преобразовали в DTO + запросили из статистики просмотры

// 3. записали в статистику +1


//        GET /events

        // Основной сервис
//        GET /events/{eventId}
//     1. Обрабатываем запрос, загружаем данные по  мероприяютию
        // 2. Запрашиваем статистику - слой сервиса EventServiceImpl (требование)
        //3. Увеличиваем hit - слой контроллера EventController (рекомендация)

        // Основной сервис
//        GET /events
//     1. Обрабатываем запрос, загружаем данные по  мероприяютия (1, 44, 100 - ид событий)
        // 2. Запрашиваем статистику по мероприятиям (Массив из /events/{eventId}) - слой сервиса
        // /events/1 ,  /events/44, /events/100
        // Отправляется все в 1 запросе в статистку
        //3. Увеличиваем hit
        // /events/1 ,  /events/44, /events/100  - не верно!!!
        // только для /events - слой контроллера
    }*/
}