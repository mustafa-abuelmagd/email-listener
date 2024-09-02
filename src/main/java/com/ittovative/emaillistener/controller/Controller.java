package com.ittovative.emaillistener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.TokenResponse;

import com.ittovative.emaillistener.config.GoogleProperties;
import com.ittovative.emaillistener.service.GmailService;
import com.ittovative.emaillistener.service.OAuth2Service;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@RestController
//@RequestMapping("/")
public class Controller {

    @Autowired
   private GmailService gmailService ;
    @Autowired
    private  OAuth2Service  oAuth2Service ;

    @Autowired
    private GoogleProperties googleOAuthProperties ;

    public Controller(GoogleProperties googleOAuthProperties , GmailService gmailService  , OAuth2Service  oAuth2Service ) {
        this.googleOAuthProperties = googleOAuthProperties ;
        this.gmailService = gmailService ;
        this.oAuth2Service = oAuth2Service ;
    }



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
            System.out.println( "token : "+  googleOAuthProperties.getToken() );
            System.out.println( "history ID  : "+  decodedString );

            gmailService.listHistoryItems( decodedString, googleOAuthProperties.getToken());

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
