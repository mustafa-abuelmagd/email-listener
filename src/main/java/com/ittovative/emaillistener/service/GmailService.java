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

    /**
     * Lists history items from Gmail starting from a specified history ID and processes new messages.
     * <p>
     * This method retrieves the Gmail history from the given start history ID and processes any new messages
     * that have been added. It updates the latest history ID and retrieves the first message's details if available.
     * </p>
     *
     * @param historyId the history ID to start listing from
     * @param token     the OAuth2 token used for authentication
     * @throws IOException if there is an error accessing Gmail or processing the history items
     */
    public void listHistoryItems(String historyId, String token) throws IOException {
        System.out.println("historyId " + historyId);
        String userId = "me";
        BigInteger startHistoryId = new BigInteger(googleProperties.getHistoryId());
        Gmail.Users.History.List request = gmailService.users().history().list(userId).setStartHistoryId(startHistoryId).setAccessToken(token);

        googleProperties.setHistoryId(historyId);

        ListHistoryResponse response = request.execute();
        List<History> histories = response.getHistory();

        if (histories != null && !histories.isEmpty()) {
            History history = histories.get(0);
            List<Message> messages = history.getMessages();
            if (messages != null && !messages.isEmpty()) {
                for (int i = 0; i < messages.size(); i++) {
                    String messageId = messages.get(i).getId(); // Get the first message ID
                    getMessageById(messageId, token);
                }

            } else {
                System.out.println("No messages found in the history.");
            }
        } else {
            System.out.println("No history items found.");
        }

    }

    /**
     * Retrieves and processes a message by its ID.
     * <p>
     * This method fetches a message from Gmail using the provided message ID, decodes the raw email data,
     * and then extracts email details using {@link ExtractEmailData}.
     * </p>
     *
     * @param messageId the ID of the message to retrieve
     * @param token     the OAuth2 token used for authentication
     */
    public void getMessageById(String messageId, String token) {

        try {

            Gmail service = new Gmail.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(),
                    new GoogleCredential().setAccessToken(token))
                    .setApplicationName("email-receiver-ittovative")
                    .build();

            // Retrieve the message by ID
            Message message = service.users().messages().get("me", messageId).setFormat("raw").execute();
            // decode email
            String rawMessage = message.getRaw();
            byte[] emailBytes = Base64.getUrlDecoder().decode(rawMessage);
            String decodedEmail = new String(emailBytes, StandardCharsets.UTF_8);


            extractEmailData.extractEmailData(decodedEmail);
            System.out.println(message.getHistoryId() + "\n\n\n\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the latest history ID from Gmail.
     * <p>
     * This method initiates a watch request to the Gmail API to get the latest history ID and sets up
     * notifications for the specified label (INBOX). The history ID is used to track changes in the Gmail account.
     * </p>
     *
     * @return the latest history ID as a string
     * @throws IOException if there is an error accessing Gmail or initiating the watch request
     */
    public String getLatestHistoryId() throws IOException {
        Profile profile = gmailService.users().getProfile("me").setAccessToken(googleProperties.getToken()).execute();
        String historyId = String.valueOf(profile.getHistoryId());
        return historyId;
    }

    public void sendWatchRequest() throws IOException {
        WatchRequest request = new WatchRequest()
                .setTopicName(googleProperties.getTopicName()) // Replace with your topic name
                .setLabelIds(Collections.singletonList("INBOX"));

        gmailService.users().watch("me", request).setAccessToken(googleProperties.getToken()).execute();

    }


}



