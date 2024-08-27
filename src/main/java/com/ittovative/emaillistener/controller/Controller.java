package com.ittovative.emaillistener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ittovative.emaillistener.service.GmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
//@RequestMapping("/")
public class Controller {

    @Autowired
    GmailService service;

    String token ="ya29.a0AcM612zdpw-HWY7qu9yon9IpnP06o5W5QWG6oTTayh-cAMw2gDc3AlaFoDEjCDRueFzpZUW1jCzCW6r9qF8WdkQOrabngg7q4J9mbqMW4E9d54tsNhdHxbA08jWTOF5g1MbEwj8dUCJnurGsZp00drBtdy8AA9WdY5BZU8LxaCgYKAXMSARESFQHGX2Mib0S2IepCRDCcEHc0rFcxHQ0175";




    @PostMapping("/receive")
    public String receiveNotification(@RequestBody Map<String, Object> jsonBody) {
        Map<String, Object> data = (Map<String, Object>) jsonBody.get("message");
        String encodedMessage = (String) data.get("data");

        System.out.println("encodedMessage 1 ::: " + encodedMessage);
        byte[] byteArr = Base64.getDecoder().decode(encodedMessage);

        String decodedString = new String(byteArr);

        System.out.println("encodedMessage 2 ::: " + decodedString);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> result = objectMapper.readValue(decodedString, Map.class);

            decodedString = (String) result.get("historyId").toString();


            System.out.println("encodedMessage 4 ::: " + decodedString);

            service.listHistoryItems(decodedString, token );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "decodedString";

    }


    @GetMapping("/login")
    public void login(HttpServletResponse httpServletResponse) {
        //Create The Google login request
        //Use The Client Id and Client Secret
        //Define The Auth URL
        //Define The scopes array
        //Define the redirect URL
        String Auth_url = "https://accounts.google.com/o/oauth2/v2/auth";
        String CLIENT_ID = "12787138394-1i1vr35m674rgp0ce27invhv9go71avr.apps.googleusercontent.com";
//        String CLIENT_SECRET = "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";

        String scopeString = "https://mail.google.com/ https://www.googleapis.com/auth/gmail.readonly";

        String redirect_url = "http://localhost:8080/redirect";

        //Use Rest Client to make the http request

        String authUrl = UriComponentsBuilder.fromHttpUrl(Auth_url)
                .queryParam("response_type", "code")
                .queryParam("access_type", "offline")
                .queryParam("client_id", CLIENT_ID)
                .queryParam("redirect_uri", redirect_url)
                .queryParam("scope", scopeString)
                .build()
                .toUriString();


        //Redirect the request to the google login request
        httpServletResponse.setHeader("Location", authUrl);
        httpServletResponse.setStatus(302);
    }


    @GetMapping("/redirect")
    public Map<String, String> getAuthorizationCode(HttpServletRequest request, HttpServletResponse response) {
        String authorization_code = request.getParameter("code");
        String scopes = request.getParameter("scope");

        String token_url = "https://oauth2.googleapis.com/token";
        String CLIENT_ID = "12787138394-1i1vr35m674rgp0ce27invhv9go71avr.apps.googleusercontent.com";
        String CLIENT_SECRET = "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";

        String scopeString = "https://mail.google.com/ https://www.googleapis.com/auth/gmail.readonly https://www.googleapis.com/auth/gmail.metadata";

        String redirect_url = "http://localhost:8080/redirect";


        String authUrl = UriComponentsBuilder.fromHttpUrl(token_url)
                .queryParam("response_type", "code")
                .queryParam("client_id", CLIENT_ID)
                .queryParam("redirect_uri", redirect_url)
                .queryParam("scope", scopes)
                .build()
                .toUriString();


        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorization_code);
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("redirect_uri", redirect_url);
        params.add("grant_type", "authorization_code");

        WebClient webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        return webClient.post()
                .uri(authUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData(params))
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
                    Map<String, String> tokenResponse = new HashMap<>();
                    tokenResponse.put("access_token", (String) response1.get("access_token"));
                    token = (String) response1.get("access_token");
                    tokenResponse.put("refresh_token", (String) response1.get("refresh_token"));
                    tokenResponse.put("expires_in", String.valueOf(response1.get("expires_in")));
                    tokenResponse.put("token_type", (String) response1.get("token_type"));
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


    }


}
