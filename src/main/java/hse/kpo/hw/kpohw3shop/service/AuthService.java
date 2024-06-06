package hse.kpo.hw.kpohw3shop.service;

import hse.kpo.hw.kpohw3shop.dto.resonse.CheckAuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final RestTemplate restTemplate;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean checkAuthorized(String token) {
        String url = "http://localhost:8080/check-auth?token={token}";

        ResponseEntity<Boolean> responseEntity =
                restTemplate.getForEntity(url, Boolean.class, token);

        if (responseEntity.hasBody()) {
            return responseEntity.getBody();
        }

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Can't get response from auth service.")
    }
}
