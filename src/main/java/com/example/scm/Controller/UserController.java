package com.example.scm.Controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.scm.Services.DefinationFolder.userServices;

@Controller
@RequestMapping("/user")
public class UserController {

  
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private  userServices uzerServices;

    // For Showing Every Details of User on every page
   
   
    // user dashboard page
    @GetMapping("/dashboard")
    public String userDashboard() {
        return "users/dashboard";
    }
    // user profile page
     @GetMapping("/profile")
    public String userProfile(Model model,Authentication authentication) {
      
        return "users/profile";
    }
 
    // user add contact page

    // user view contact page

    // user edit contact page

    // user delete contact page

    // user search contact page
}
