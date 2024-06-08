package hse.kpo.hw.kpohw3shop.service;

import hse.kpo.hw.kpohw3shop.dto.response.CheckAuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthService {
    private final RestTemplate restTemplate;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Возвращаем Optional<String>, в котором пусто в случае,
    // если пользователя с таким токеном не авторизован, иначе - имя пользователя.
    public Optional<Integer> checkAuthorized(String token) {
        String host = System.getenv("AUTH_host");
        String url = "http://" + host + ":8081/check-auth?token={token}";

        try {
            ResponseEntity<CheckAuthResponse> responseEntity =
                    restTemplate.getForEntity(url, CheckAuthResponse.class, token);

            if (responseEntity.hasBody() && responseEntity.getBody() != null) {

                if (responseEntity.getBody().getIsAuthorized()) {
                    return Optional.of(responseEntity.getBody().getUserId());
                }

                return Optional.empty();
            }
        }
        catch (RestClientException ignored) {
        }

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Can't get response from auth service.");
    }
}
