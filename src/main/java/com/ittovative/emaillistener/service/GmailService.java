package com.ittovative.emaillistener.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class GmailService {

    String latestHistoryId = "1012355";
    private final Gmail gmailService;


    public GmailService(Gmail gmailService) {
        this.gmailService = gmailService;
    }

    public void listHistoryItems(String historyId, String token) throws IOException {
        System.out.println("The history id is come : " + historyId);

        //  latestHistoryId = historyId;


        String userId = "me";
        BigInteger startHistoryId = new BigInteger(latestHistoryId);
        Gmail.Users.History.List request = gmailService.users().history().list(userId).setStartHistoryId(startHistoryId).setAccessToken(token);

        latestHistoryId = historyId;

        ListHistoryResponse response = request.execute();
        List<History> histories = response.getHistory();

        if (histories != null && !histories.isEmpty()) {
            History history = histories.get(0); // Access the first history item
            List<HistoryMessageAdded> messagesAdded = history.getMessagesAdded();

            if (messagesAdded != null && !messagesAdded.isEmpty()) {
                String messageId = messagesAdded.get(0).getMessage().getId(); // Get the first message ID
                System.out.println("Message ID: " + messageId);

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

            System.out.println("decodeEmail ");


            System.out.println("Decoded Email: " + decodedEmail);

            extractEmailData(decodedEmail) ;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void extractEmailData(String emailContent) {
        String deliveredTo = extractField(emailContent, "Delivered-To:");
        String received = extractField(emailContent, "Received:");
        String from = extractField(emailContent, "From:");
        String subject = extractField(emailContent, "Subject:");


        System.out.println("Delivered-To: " + deliveredTo);
        System.out.println("Received: " + received);
        System.out.println("From: " + from);
        System.out.println("Subject: " + subject);


    }

    private String extractField(String content, String fieldName) {
        Pattern pattern = Pattern.compile(fieldName + " (.+)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "Not Found";
    }





}
