package com.ittovative.emaillistener.controller;

import com.google.api.client.auth.oauth2.TokenResponse;

import com.ittovative.emaillistener.config.GoogleProperties;
import com.ittovative.emaillistener.service.GmailService;
import com.ittovative.emaillistener.service.GoogleService;
import com.ittovative.emaillistener.service.OAuth2Service;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
//@RequestMapping("/")
public class SystemController {


   private final GmailService gmailService ;
   private final  OAuth2Service  oAuth2Service ;
   private final GoogleProperties googleOAuthProperties ;
   private  final  GoogleService googleService ;

    @Autowired
    public SystemController(GoogleProperties googleOAuthProperties , GmailService gmailService
            , OAuth2Service  oAuth2Service, GoogleService googleService) {
        this.googleOAuthProperties = googleOAuthProperties ;
        this.gmailService = gmailService ;
        this.oAuth2Service = oAuth2Service ;
        this.googleService = googleService;
    }



    @PostMapping("/receive")
    public String receiveNotification(@RequestBody Map<String, Object> jsonBody) throws IOException {

        String  decodedString =   googleService.decodeHistoryId( jsonBody ) ;

            gmailService.listHistoryItems( decodedString, googleOAuthProperties.getToken());

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
        String accrss_token = token.getAccessToken();
        googleOAuthProperties.setToken(accrss_token);
        googleOAuthProperties.setHistoryId( gmailService.GetLatestHistoryId() );


        return  token ;

    }


}
