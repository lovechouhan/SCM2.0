package com.example.scm.Services.DefinationFolder;

public interface OTPservices {

    void sendOTPEmail(String email);
    void sendEmailwithOTP(String to, String subject, String body);

}
