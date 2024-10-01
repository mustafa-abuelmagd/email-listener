package com.ittovative.emaillistener.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExtractEmailData {
    /**
     * Component for extracting and processing email data from email content.
     * <p>
     * This class contains methods to extract specific fields and the message body
     * from raw email content. It uses regular expressions to parse and retrieve
     * information such as "Delivered-To", "Received", "From", "Subject", and the
     * message body.
     * </p>
     */
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
    /**
     * Extracts the value of a specific field from the email content.
     * <p>
     * This method uses a regular expression to search for the specified field name
     * and retrieve its value from the email content. The field name is followed by a
     * space and the value to be extracted. If the field is not found, "Not Found" is returned.
     * </p>
     *
     * @param content   the raw email content as a string
     * @param fieldName the name of the field to extract (e.g., "From:", "Subject:")
     * @return the value of the field, or "Not Found" if the field is not present
     */
    private String extractField(String content, String fieldName) {
        Pattern pattern = Pattern.compile(fieldName + " (.+)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "Not Found";
    }
    /**
     * Extracts the body of the email message from the email content.
     * <p>
     * This method uses a regular expression to retrieve the message body from the
     * email content. It assumes the content type is "text/plain" and extracts
     * the body until the start of a new section or the end of the content.
     * The message body is extracted from the part of the content that follows the
     * "Content-Type: text/plain; charset=\"UTF-8\"" header and ends before the next section.
     * If the message body cannot be found, "Message Not Found" is returned.
     * </p>
     *
     * @param content the raw email content as a string
     * @return the message body, or "Message Not Found" if the body cannot be extracted
     */
    private String extractMessageBody(String content) {
        Pattern pattern = Pattern.compile("Content-Type: text/plain; charset=\"UTF-8\"\\s*(.*?)\\s*(?=--\\w{12,}|Content-|$)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "Message Not Found";
    }


}
