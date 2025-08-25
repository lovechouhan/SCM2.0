package com.example.scm.Services.Implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.scm.Services.DefinationFolder.EmailService;

@Service
public class EmailServiceIMPL implements EmailService {

    @Autowired
    private  JavaMailSender mailSender;

  

    @Override
    public void sendEmail(String to, String subject, String body) {

        SimpleMailMessage message  = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("lc46200517@gmail.com");


        mailSender.send(message);
    }

   
}
