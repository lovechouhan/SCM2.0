package com.example.scm.Controller;


import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.scm.Entities.Providers;
import com.example.scm.Entities.User;
import com.example.scm.Services.DefinationFolder.userServices;
import com.example.scm.helper.Helper;
import com.example.scm.helper.msg;
import com.example.scm.helper.msgType;

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

    // user settings page
    @GetMapping("/settings")
    public String userSettings(Model model, Authentication authentication) {
        return "users/settings";
    }


    // change password page
    @PostMapping("/change-password")
    public String userChangePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,Model model, Authentication authentication,Principal principal) {
        // Implement password change logic here

        System.out.println("Old Password: " + oldPassword);
        System.out.println("New Password: " + newPassword);

         if(authentication == null) return "redirect:/AlreadyAuthenticated";

        System.out.println("Adding logged in user information to model");
        String username = Helper.getEmailofLoggedInUser(authentication);
        logger.info("$$$$$$$$$$$ {}");

        User user = uzerServices.getUserByEmail(username);
         
        System.out.println(user);
        System.out.println(user.getName());
        System.out.println(user.getEmail());

        String password = user.getPassword();
        Providers provider = user.getProvider();

        System.out.println("Current Password: " + password);
        System.out.println("Provider: " + provider);

        if(provider != Providers.SELF){
            System.out.println("User is not self-managed");
            model.addAttribute("message", new msg("❌ You cannot change password for " + provider + " authenticated accounts.", msgType.red));
            return "users/AlreadyAuthenticated";
        }
        if(password !=null){
            if(password.equals(oldPassword)){
                user.setPassword(newPassword);
                uzerServices.updateUser(user);
                model.addAttribute("message", new msg("✅ Password changed successfully.", msgType.green));
                return "redirect:/user/profile";
            }else{
                model.addAttribute("message", new msg("❌ Old password is incorrect.", msgType.red));
                return "redirect:/user/profile";
            }
        }

        return "redirect:/user/profile";

    }

    
    
}
