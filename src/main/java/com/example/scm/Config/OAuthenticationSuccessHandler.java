package com.example.scm.Config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.scm.Entities.Providers;
import com.example.scm.Entities.User;
import com.example.scm.Reposititers.UserRepo;
import com.example.scm.helper.AppConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthenticationSuccessHandler implements  AuthenticationSuccessHandler {

  


    Logger logger = Logger.getLogger(OAuthenticationSuccessHandler.class.getName());

      @Autowired
      private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
     HttpServletResponse response,
    Authentication authentication) throws IOException, ServletException {



     
        logger.info("Authentication successful for user: " + authentication.getName());


        // first identify the authentication provider
        // google, github, facebook, etc.



        var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String provider = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        logger.info("Provider: " + provider);


        var oauthUser = (DefaultOAuth2User)authentication.getPrincipal();

        oauthUser.getAttributes().forEach((key, value) -> {
            logger.info("Key: " + key + ", Value: " + value);
        });


        User user = new User();

        user.setUserid(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setEmailVerified(true);
        user.setEnabled(true);
       
        


        if (provider.equals("google")) {

         // 1 google 
        // google attributes
   
        user.setEmail(oauthUser.getAttribute("email").toString());
        user.setProfilePic(oauthUser.getAttribute("picture").toString());
      user.setName(oauthUser.getAttribute("name").toString());
        user.setProviderId(oauthUser.getName());
         user.setProvider(Providers.GOOGLE);
         user.setEnabled(true);

        } else if (provider.equals("github")) {

            // 2 github
            // github attributes
            String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString() : oauthUser.getAttribute("login").toString() + "@gmail.com";
            String picture  = oauthUser.getAttribute("avatar_url").toString() ;
            String name = oauthUser.getAttribute("login").toString();
            String provider_id = oauthUser.getName();

            user.setEmail(email);
            user.setProfilePic(picture);
            user.setName(name);
            user.setProviderId(provider_id);
            user.setProvider(Providers.GITHUB);
            user.setEnabled(true);

           
        } else if (provider.equals("linkedin")) {

            // 3 linkedIn
            // LinkedIn attributes
           
        } 
        else {
           logger.warning("Unknown authentication provider: " + provider);
        }











         // check if the user already exists in the database
          User user2 = userRepo.findByEmail(user.getEmail()).orElse(null);
        if (user2 == null) {
            // if the user already exists, update their information
            userRepo.save(user);
            logger.info("User saved successfully: " + user.getEmail());
        } 







        // Redirect to the home page or any other page after successful authentication

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
    }

}
