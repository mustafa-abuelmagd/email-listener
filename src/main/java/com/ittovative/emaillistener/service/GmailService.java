package com.ittovative.emaillistener.service;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;
import com.ittovative.emaillistener.config.GoogleProperties;
import com.ittovative.emaillistener.util.ExtractEmailData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;



@Service
public class GmailService {

    private final Gmail gmailService;
    private final GoogleProperties googleProperties;
    private final ExtractEmailData extractEmailData;
    private String latestHistoryId = null;

    @Autowired
    public GmailService(Gmail gmailService, GoogleProperties googleProperties, ExtractEmailData extractEmailData) {
        this.gmailService = gmailService;
        this.googleProperties = googleProperties;
        this.extractEmailData = extractEmailData;
    }

    public void listHistoryItems(String historyId, String token) throws IOException {

             if( latestHistoryId == null){
                latestHistoryId = googleProperties.getHistoryId() ;

             }


        String userId = "me";
        BigInteger startHistoryId = new BigInteger(latestHistoryId);
        Gmail.Users.History.List request = gmailService.users().history().list(userId).setStartHistoryId(startHistoryId).setAccessToken(token);

        latestHistoryId = historyId;

        ListHistoryResponse response = request.execute();
        List<History> histories = response.getHistory();

        if (histories != null && !histories.isEmpty()) {
            History history = histories.get(0);
            List<HistoryMessageAdded> messagesAdded = history.getMessagesAdded();

            if (messagesAdded != null && !messagesAdded.isEmpty()) {
                String messageId = messagesAdded.get(0).getMessage().getId(); // Get the first message ID

                getMessageById(messageId, token);
            } else {
                System.out.println("No messages found in the history.");
            }
        } else {
            System.out.println("No history items found.");
        }

    }


    public void getMessageById(String messageId, String token) {

        try {

            Gmail service = new Gmail.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(),
                    new GoogleCredential().setAccessToken(token))
                    .setApplicationName("listeningemail-2")
                    .build();

            // Retrieve the message by ID
            Message message = service.users().messages().get("me", messageId).setFormat("raw").execute();

            // decode email
            String rawMessage = message.getRaw();
            byte[] emailBytes = Base64.getUrlDecoder().decode(rawMessage);
            String decodedEmail = new String(emailBytes, StandardCharsets.UTF_8);


            extractEmailData.extractEmailData(decodedEmail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String GetLatestHistoryId() throws IOException {

        WatchRequest request = new WatchRequest()
                .setTopicName( googleProperties.getTopicName() ) // Replace with your topic name
                .setLabelIds(Collections.singletonList("INBOX"));


        WatchResponse response = gmailService.users().watch("me", request).setAccessToken(googleProperties.getToken()).execute();

        String  historyId = response.getHistoryId().toString() ;
        return historyId;
    }


}



