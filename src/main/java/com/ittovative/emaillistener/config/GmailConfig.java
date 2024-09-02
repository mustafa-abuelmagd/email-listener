package com.ittovative.emaillistener.config;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GmailConfig {


    private GoogleProperties googleOAuthProperties;

    @Autowired
    public GmailConfig(@Qualifier("googleProperties") GoogleProperties googleOAuthProperties) {
        this.googleOAuthProperties = googleOAuthProperties;
    }

    @Bean
    public Gmail gmail() throws Exception {
        NetHttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();


        // Create a credential object using the token from OAuth2Service
        Credential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(googleOAuthProperties.getClientId(), googleOAuthProperties.getClientSecret())
                .build()
                .setAccessToken(googleOAuthProperties.getToken());

        // Build and return the Gmail service
        return new Gmail.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Email Listener")
                .build();
    }
}