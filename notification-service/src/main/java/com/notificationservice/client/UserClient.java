package com.notificationservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserClient {

    private final WebClient webClient;

    public String getUserEmail(Long userId) {
        return webClient.get()
                .uri("http://USER-SERVICE/api/users/" + userId + "/email")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

