package com.ittovative.emaillistener.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;

@Service
public class GoogleService {
    /**
     * Decodes a history ID from a JSON body received from Google.
     * <p>
     * This method extracts an encoded message from the provided JSON body, decodes it from Base64,
     * and then parses the decoded string as JSON to retrieve the history ID. The history ID is used
     * to track changes in Gmail.
     * </p>
     *
     * @param jsonBody a map representing the JSON body containing the encoded message
     * @return the decoded history ID as a string
     */
    public String decodeHistoryId(Map<String , Object> jsonBody ){

        Map<String, Object> data = (Map<String, Object>) jsonBody.get("message");
        String encodedMessage = (String) data.get("data");

        byte[] byteArr = Base64.getDecoder().decode(encodedMessage);

        String decodedString = new String(byteArr);


        try {
            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> result = objectMapper.readValue(decodedString, Map.class);

            decodedString = (String) result.get("historyId").toString();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return  decodedString ;
    }

}
