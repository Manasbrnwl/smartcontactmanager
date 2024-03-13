package com.smart.smartcontactmanager.config;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    // this is responsible for send email
    public boolean sendEmail(String message, String subject, String to) {

        boolean f = false;

        String from = "manas.codepractice@gmail.com";

        // Variable form gmail
        String host = "smtp.gmail.com";

        // get the system properties
        Properties properties = System.getProperties();
        System.out.println("properties : " + properties);

        // setting important information to properties

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Step 1 : to get the session object
        Session session = Session.getInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "gehpslsyqirvivmi");
            }

        });

        session.setDebug(true);
        // step 2 : compose the message [text, multi media]
        MimeMessage m = new MimeMessage(session);

        try {
            // from email id
            m.setFrom(from);

            // adding Recipient
            m.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // adding subject
            m.setSubject(subject);

            // adding message
            m.setText(message);

            // send

            // step 3: send the message using transport class

            Transport.send(m);

            System.out.println("send");
            f = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }
}
