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


    private final GmailService gmailService;
    private final OAuth2Service oAuth2Service;
    private final GoogleProperties googleOAuthProperties;
    private final GoogleService googleService;

    @Autowired
    public SystemController(GoogleProperties googleOAuthProperties, GmailService gmailService
            , OAuth2Service oAuth2Service, GoogleService googleService) {
        this.googleOAuthProperties = googleOAuthProperties;
        this.gmailService = gmailService;
        this.oAuth2Service = oAuth2Service;
        this.googleService = googleService;
    }


    /**
     * Receives notifications from a webhook and processes them.
     * <p>
     * This endpoint is used to handle incoming notifications. It decodes the history ID
     * from the request body and uses it to list history items from Gmail using the token
     * stored in {@link GoogleProperties}.
     * </p>
     *
     * @param jsonBody a map containing the JSON body of the notification
     * @return a confirmation string indicating successful processing
     * @throws IOException if there is an error processing the notification or communicating with Gmail
     */
    @PostMapping("/receive")
    public String receiveNotification(@RequestBody Map<String, Object> jsonBody) throws IOException {

        String decodedString = googleService.decodeHistoryId(jsonBody);

        gmailService.listHistoryItems(decodedString, googleOAuthProperties.getToken());

        return "decodedString";

    }


    /**
     * Redirects the user to the OAuth2 authorization URL for login.
     * <p>
     * This endpoint generates an authorization URL and redirects the user to it. The user
     * needs to authorize the application and obtain an authorization code.
     * </p>
     *
     * @param httpServletResponse the HTTP response used to redirect the user
     */
    @GetMapping("/login")
    public void login(HttpServletResponse httpServletResponse) {

        String authUrl = oAuth2Service.generateAuthUrl();

        httpServletResponse.setHeader("Location", authUrl);
        httpServletResponse.setStatus(302);
    }


    /**
     * Exchanges the authorization code for an access token and updates application properties.
     * <p>
     * This endpoint handles the authorization code returned from the OAuth2 provider,
     * exchanges it for an access token, and updates the {@link GoogleProperties} with the
     * new token and the latest history ID from Gmail.
     * </p>
     *
     * @param code the authorization code received from the OAuth2 provider
     * @return the {@link TokenResponse} containing the access token
     * @throws IOException if there is an error exchanging the authorization code or retrieving the token
     */
    @GetMapping("/redirect")
    public TokenResponse getAuthorizationCode(@RequestParam String code) throws IOException {
        TokenResponse token = oAuth2Service.exchangeCodeToToken(code);
        String access_token = token.getAccessToken();

        gmailService.sendWatchRequest();

        googleOAuthProperties.setToken(access_token);
        googleOAuthProperties.setHistoryId(gmailService.getLatestHistoryId());

        return token;

    }


}
