package com.smart.smartcontactmanager.controller;

import org.springframework.web.bind.annotation.RestController;

import com.smart.smartcontactmanager.config.EmailService;
import com.smart.smartcontactmanager.entities.EmailRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @RequestMapping("/welcome")
    public String welcome() {
        return "hello this is my email api";
    }

    // api to send email
    @PostMapping("/sendmail")
    public ResponseEntity<?> sendmail(@RequestBody EmailRequest request) {
        boolean result = this.emailService.sendEmail(request.getMessage(), request.getSubject(), request.getTo());
        if (result) {
            return ResponseEntity.ok("Done...");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error...");
    }

}
