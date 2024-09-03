package com.ittovative.emaillistener.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExtractEmailData {

    public void extractEmailData(String emailContent) {
        String deliveredTo = extractField(emailContent, "Delivered-To:");
        String received = extractField(emailContent, "Received:");
        String from = extractField(emailContent, "From:");
        String subject = extractField(emailContent, "Subject:");
        String message = extractMessageBody(emailContent);

        System.out.println("From: " + from);
        System.out.println("Delivered-To: " + deliveredTo);
        System.out.println("Received: " + received);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message   );



    }

    private String extractField(String content, String fieldName) {
        Pattern pattern = Pattern.compile(fieldName + " (.+)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "Not Found";
    }
    private String extractMessageBody(String content) {
        Pattern pattern = Pattern.compile("Content-Type: text/plain; charset=\"UTF-8\"\\s*(.*?)\\s*(?=--\\w{12,}|Content-|$)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "Message Not Found";
    }


}
