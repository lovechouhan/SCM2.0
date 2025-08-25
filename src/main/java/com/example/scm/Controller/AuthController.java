package com.example.scm.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.scm.Entities.User;
import org.slf4j.Logger;
import com.example.scm.Services.DefinationFolder.userServices;



@Controller
@RequestMapping("/auth")
public class AuthController {


    private Logger logger = org.slf4j.LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private userServices userService;

    // verify email
   @GetMapping("/verify-email")
   public String verifyEmail(@RequestParam("token") String token) {
        // Logic to verify email using the token
       User user = userService.findByEmailToken(token);
        if(user==null){
            logger.warn("No user found with email token: {}", token);
            return "error_page";
        }
        if(user.getEmailToken().equals(token)){
            user.setEmailVerified(true);
            user.setEnabled(true);
            userService.updateUser(user);
            logger.info("Email verification succeeded for user with token: {}", token);     

            return "success_page";
        }
        return "error_page";
   }
}

