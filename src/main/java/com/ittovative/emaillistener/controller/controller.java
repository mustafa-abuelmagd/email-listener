package com.ittovative.emaillistener.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
//@RequestMapping("/")
public class controller {


    @PostMapping("/receive")
    public String receiveNotification(@RequestBody String message) {
//        System.out.println("received a new message:::    " + message);
//
//        String data = message.toString().split("\"data\":")[1].split(",")[0];
//        System.out.println("\n\n data:::    " + data);
//
//        data = data.substring(2, data.length() - 1);
//        System.out.println("\n\n data:::    " + data);
//
//        byte[] byteArr = Base64.getDecoder().decode(data);
//        for(int i = 0 ; i <byteArr.length; i++ ){
//            System.out.println("byte arr [i] :::: " + byteArr[i]);
//        }
//        String decodedString = new String(byteArr);
// AIzaSyBi1ge7N1RQWUPB3FiXOXn1bBPSgzhYhKc
//        System.out.println("received a new message:::    " + decodedString);


        return "decodedString";

    }

    @GetMapping("/receive")
    public String getNotification() {

        return "decodedString";

    }
}
