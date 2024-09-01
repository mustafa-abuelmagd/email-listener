package com.ittovative.emaillistener.service;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



@Service
public class OAuth2Service {
    private static final String AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String CLIENT_ID = "12787138394-1i1vr35m674rgp0ce27invhv9go71avr.apps.googleusercontent.com";
    private static final List<String> SCOPES = Arrays.asList(
            "https://mail.google.com/",
            "https://www.googleapis.com/auth/gmail.readonly"
    );
    private static final String REDIRECT_URL = "http://localhost:8080/redirect";
    private static final String RESPONSE_TYPE = "code";


    String CLIENT_SECRET = "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";

    public String generateAuthUrl( ) {

        AuthorizationCodeRequestUrl authorizationCodeRequestUrl =
                new AuthorizationCodeRequestUrl(AUTH_URL, CLIENT_ID)
                        .setRedirectUri(REDIRECT_URL)
                        .setResponseTypes(Collections.singleton(RESPONSE_TYPE))
                        .setScopes(SCOPES)
                        .set("access_type", "offline");

        return authorizationCodeRequestUrl.build();
    }

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private AuthorizationCodeFlow getAuthorizationCodeFlow() {
        return new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPES)
                .setAccessType("offline")
                .build();
    }

    public TokenResponse exchangeCodeToToken(String authorizationCode) throws IOException, IOException {
        AuthorizationCodeFlow authorizationCodeFlow = getAuthorizationCodeFlow();
        TokenResponse tokenResponse = authorizationCodeFlow.newTokenRequest(authorizationCode)
                .setRedirectUri(REDIRECT_URL)
                .execute();

        // Extract and return the access token
        return tokenResponse ;
    }

}