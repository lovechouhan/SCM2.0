package com.example.scm.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public class Helper {

    public static String getEmailofLoggedInUser(Authentication authentication) {
        
      
     
       // agr email  & password se login kiya hai toh : Email kaise nikalege

       if(authentication instanceof OAuth2AuthenticationToken){

          var aOAuth2AuthenticationToken =  (OAuth2AuthenticationToken)authentication;
              var provider = aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
              var oauth2User = aOAuth2AuthenticationToken.getPrincipal();
              String username = "";
       
        // agr google se login kiya hai toh : email kaise nikalege
        if(provider.equalsIgnoreCase("google")){
              System.out.println("email from google");
              username = oauth2User.getAttribute("email").toString();  
        }

        else if(provider.equalsIgnoreCase("github")){
             System.out.println("email from github"); 
             // agr github se login kiya hai toh : email kaise nikalege
          username = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email").toString() : oauth2User.getAttribute("login").toString() + "@gmail.com";
        }
        

        return username;
    
        }


        // agr email & password se login kiya hai toh : email kaise nikalege
        else{
            System.out.println("get data from local db");
            return authentication.getName(); // or your actual logic to get email
        }
    }


    // Email Verification
    public static String getLinkForEmailVerification(String emailToken) {
      // String link = "https://contact-manager-scm.up.railway.app/auth/verify-email?token=" + emailToken;
       String link = "http://localhost:8080/auth/verify-email?token=" + emailToken;
      return link;
    }
}
