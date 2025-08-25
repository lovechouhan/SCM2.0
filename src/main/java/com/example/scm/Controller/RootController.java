package com.example.scm.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.scm.Entities.User;
import com.example.scm.Services.DefinationFolder.userServices;
import com.example.scm.helper.Helper;

// iss method ko har request se pehle call kiya jayega user ki info dikhane ke liye har page par

// pure project me har ek request ke liyeye method execute hoga
// isliye hum isko @controlleradvice annotation me rakhte hain
@ControllerAdvice
public class RootController {

    private static  Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private  userServices uzerServices;

    // For Showing Every Details of User on every page
    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {

        // agar user login nahi hai to authentication null hoga
        // isliye agar authentication null hai to return kar do
        if(authentication == null) return;
    
        System.out.println("Adding logged in user information to model");
        String username = Helper.getEmailofLoggedInUser(authentication);
        logger.info("Logged in user email: {}", username);
        
        User user = uzerServices.getUserByEmail(username);
         
        System.out.println(user);
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        model.addAttribute("loggedInUser", user);  // user ki key ""loggedInUser"" hai
        
    }
}
