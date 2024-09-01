package com.ittovative.emaillistener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.ittovative.emaillistener.service.GmailService;
import com.ittovative.emaillistener.service.OAuth2Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
//@RequestMapping("/")
public class Controller {

    @Autowired
    GmailService service;
    @Autowired
    OAuth2Service  oAuth2Service ;

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


        String authUrl = oAuth2Service.generateAuthUrl();


        httpServletResponse.setHeader("Location", authUrl);
        httpServletResponse.setStatus(302);
    }

    @GetMapping("/redirect")
    public TokenResponse getAuthorizationCode(@RequestParam String  code ) throws IOException {


        TokenResponse token =  oAuth2Service.exchangeCodeToToken(code) ;
        return  token ;

    }


}
