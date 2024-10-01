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


    /**
     * Generates the URL for the OAuth2 authorization request.
     * <p>
     * This method constructs the URL that users need to visit in order to authorize the application
     * and obtain an authorization code. The URL includes the required parameters such as client ID,
     * redirect URI, response type, and scopes.
     * </p>
     *
     * @return the authorization URL as a string
     */
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

    /**
     * Creates and returns an {@link AuthorizationCodeFlow} object.
     * <p>
     * This method initializes an {@link AuthorizationCodeFlow} using the configured client ID, client
     * secret, and scopes. The flow is used to handle the OAuth2 authorization code exchange and token
     * management.
     * </p>
     *
     * @return an {@link AuthorizationCodeFlow} instance
     */
    @Autowired
    public AuthorizationCodeFlow getAuthorizationCodeFlow() {
        return new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, googleOAuthProperties.getClientId(), googleOAuthProperties.getClientSecret(), googleOAuthProperties.getScopes())
                .setAccessType("offline")
                .build();
    }

    /**
     * Exchanges an authorization code for an access token.
     * <p>
     * This method uses the provided authorization code to request an access token from Google's OAuth2
     * service. The token response is then used to set the access token in the {@link GoogleProperties}
     * configuration.
     * </p>
     *
     * @param authorizationCode the authorization code obtained from the OAuth2 authorization server
     * @return the token response containing the access token
     * @throws IOException if there is an error during the token request or processing
     */
    public TokenResponse exchangeCodeToToken(String authorizationCode) throws IOException, IOException {
        AuthorizationCodeFlow authorizationCodeFlow = getAuthorizationCodeFlow();
        TokenResponse tokenResponse = authorizationCodeFlow.newTokenRequest(authorizationCode)
                .setRedirectUri(googleOAuthProperties.getRedirectUrl())
                .execute();


        googleOAuthProperties.setToken(tokenResponse.getAccessToken());

        return tokenResponse;
    }

}