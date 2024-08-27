package com.ittovative.emaillistener.service;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GmailService {
    String latestHistoryId = "1724484";

    public void listHistoryItems(String historyId, String token) {
        String endpoint = "https://gmail.googleapis.com/gmail/v1/users/me/history";

        if (latestHistoryId.equals("")) {
            latestHistoryId = historyId;
        }

        endpoint = UriComponentsBuilder.fromHttpUrl(endpoint)
                .queryParam("startHistoryId", latestHistoryId)
                .build()
                .toUriString();

        latestHistoryId = historyId;

        WebClient webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();


        Map<String, Object> response = webClient.get()
                .uri(endpoint)
                .header("Authorization", "Bearer " + token)
//                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .accept(MediaType.ALL)

                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), responses -> {
                    // Handle specific error responses
                    return responses.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException("Error: " + errorBody)));
                })
                .bodyToMono(Map.class)
                .map(response1 -> {
                    Map<String, Object> tokenResponse = new HashMap<>();
                    tokenResponse.put("history", (Object) response1.get("history"));
                    return tokenResponse;
                })
                .onErrorResume(UnsupportedMediaTypeException.class, ex -> {
                    // Handle the unsupported media type
                    return Mono.error(new RuntimeException("Unexpected content type: " + ex.getMessage()));
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    // Handle any other WebClient-related exceptions
                    return Mono.error(new RuntimeException("HTTP status error: " + ex.getStatusCode()));
                })
                .doOnError(ex -> {
                    // Log the error or perform additional actions
                    System.err.println("Error occurred: " + ex.getMessage());
                })
                .block(Duration.ofSeconds(20));

        response.forEach((key, value) -> System.out.println("key: " + key + " : value: " + value));

        List<Map<String, Object>> historyItems = (List<Map<String, Object>>) response.get("history");
        List<Map<String, Object>> messages = (List<Map<String, Object>>) historyItems.get(0).get("messages");
        List<Map<String, Object>> messagesAdded = (List<Map<String, Object>>) historyItems.get(0).get("messagesAdded");
        List<Map<String, Object>> messagesDeleted = (List<Map<String, Object>>) historyItems.get(0).get("messagesDeleted");
        List<Map<String, Object>> labelsAdded = (List<Map<String, Object>>) historyItems.get(0).get("labelsAdded");
        List<Map<String, Object>> labelsRemoved = (List<Map<String, Object>>) historyItems.get(0).get("labelsRemoved");

        String latestMessageId = (String) messages.get(0).get("id");

        getMessageById(latestMessageId, token);



    }

    public void getMessageById(String messageId, String token) {
        String endpoint = "https://gmail.googleapis.com/gmail/v1/users/me/messages/" + messageId;
        endpoint = UriComponentsBuilder.fromHttpUrl(endpoint)
                .queryParam("format", "minimal")

                .build()
                .toUriString();


        WebClient webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();


        Map<String, Object> response = webClient.get()
                .uri(endpoint)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.ALL)

                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), responses -> {
                    // Handle specific error responses
                    return responses.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException("Error: " + errorBody)));
                })
                .bodyToMono(Map.class)
                .map(response1 -> {
                    Map<String, Object> messageResponse = new HashMap<>();
                    response1.forEach((key, value) -> messageResponse.put(key.toString(), value));
                    return messageResponse;
                })
                .onErrorResume(UnsupportedMediaTypeException.class, ex -> {
                    // Handle the unsupported media type
                    return Mono.error(new RuntimeException("Unexpected content type: " + ex.getMessage()));
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    // Handle any other WebClient-related exceptions
                    return Mono.error(new RuntimeException("HTTP status error: " + ex.getStatusCode()));
                })
                .doOnError(ex -> {
                    // Log the error or perform additional actions
                    System.err.println("Error occurred: " + ex.getMessage());
                })
                .block(Duration.ofSeconds(20));


        response.forEach((key, value) -> System.out.println("key: " + key + " : value: " + value));

//        List<Map<String, Object>> messages = (List<Map<String, Object>>) response.get("messages");
//
//        messages.forEach(message -> System.out.println(message.get("id")));


    }


}
