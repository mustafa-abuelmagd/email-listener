package com.ittovative.emaillistener.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;

@Service
public class GoogleService {

    public String decodeHistoryId(Map<String , Object> jsonBody ){

        Map<String, Object> data = (Map<String, Object>) jsonBody.get("message");
        String encodedMessage = (String) data.get("data");

        System.out.println("encodedMessage 1 ::: " + encodedMessage);
        byte[] byteArr = Base64.getDecoder().decode(encodedMessage);

        String decodedString = new String(byteArr);

        System.out.println("encodedMessage 2 ::: " + decodedString);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> result = objectMapper.readValue(decodedString, Map.class);

            decodedString = (String) result.get("historyId").toString();


            System.out.println( "history ID  : "+  decodedString );


        } catch (Exception e) {
            e.printStackTrace();
        }

        return  decodedString ;
    }

}
