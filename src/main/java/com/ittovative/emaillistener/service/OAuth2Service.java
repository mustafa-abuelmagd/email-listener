package com.ittovative.emaillistener.service;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.ittovative.emaillistener.config.GoogleProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;


@Service
public class OAuth2Service {


    @Autowired
    private GoogleProperties googleOAuthProperties;

    public OAuth2Service(GoogleProperties googleOAuthProperties) {
        this.googleOAuthProperties = googleOAuthProperties;
    }


    //String CLIENT_SECRET = "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";

    public String generateAuthUrl() {

        AuthorizationCodeRequestUrl authorizationCodeRequestUrl =
                new AuthorizationCodeRequestUrl(googleOAuthProperties.getAuthUrl(), googleOAuthProperties.getClientId())
                        .setRedirectUri(googleOAuthProperties.getRedirectUrl())
                        .setResponseTypes(Collections.singleton(googleOAuthProperties.getResponseType()))
                        .setScopes(googleOAuthProperties.getScopes())
                        .set("access_type", "offline");

        return authorizationCodeRequestUrl.build();
    }

    public HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public JsonFactory JSON_FACTORY = new JacksonFactory();


    @Autowired
    public AuthorizationCodeFlow getAuthorizationCodeFlow() {
        return new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, googleOAuthProperties.getClientId(), googleOAuthProperties.getClientSecret(), googleOAuthProperties.getScopes())
                .setAccessType("offline")
                .build();
    }

    public TokenResponse exchangeCodeToToken(String authorizationCode) throws IOException, IOException {
        AuthorizationCodeFlow authorizationCodeFlow = getAuthorizationCodeFlow();
        TokenResponse tokenResponse = authorizationCodeFlow.newTokenRequest(authorizationCode)
                .setRedirectUri(googleOAuthProperties.getRedirectUrl())
                .execute();


        googleOAuthProperties.setToken(tokenResponse.getAccessToken());

        return tokenResponse;
    }

}