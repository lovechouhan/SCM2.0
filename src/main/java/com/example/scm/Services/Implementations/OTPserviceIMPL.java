package com.example.scm.Services.Implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.scm.Reposititers.UserRepo;
import com.example.scm.Services.DefinationFolder.OTPservices;
import com.example.scm.Services.DefinationFolder.userServices;

@Service
public class OTPserviceIMPL implements  OTPservices{

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private userServices userService;
    @Override
    public void sendOTPEmail(String email) {
        
       int OTP = (int)(Math.random()*9000)+1000;

       // Save the OTP to the user entity
       userService.updateUserOTP(email, OTP);

       String subject = "Your OTP Code";
       String body = "Your OTP code is: " + OTP;
       sendEmailwithOTP(email, subject, body);
       System.out.println("OTP sent to email: " + email + " OTP: " + OTP);

    }

    @Override
    public void sendEmailwithOTP(String to, String subject, String body) {
        
       
        SimpleMailMessage message  = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("SCM@gmail.com");


        mailSender.send(message);
    }
}
