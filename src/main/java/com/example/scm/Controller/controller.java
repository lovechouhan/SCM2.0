package com.example.scm.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.example.scm.Entities.Providers.SELF;
import com.example.scm.Entities.User;
import com.example.scm.Form.userForm;
import com.example.scm.Services.DefinationFolder.OTPservices;
import com.example.scm.Services.DefinationFolder.userServices;
import com.example.scm.helper.SessionHelper;
import com.example.scm.helper.msg;
import com.example.scm.helper.msgType;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;





@Controller
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor

public class controller {


    @Autowired
    private userServices userService;

    @Autowired
     private PasswordEncoder passwordEncoder;

     @Autowired
     private OTPservices otpService;

    @GetMapping("/")
    public String index(Model model) {
        return "redirect:/home";
    }

    @GetMapping("/About")
    public String nav() {
        return "about";
    }
    @GetMapping("/base")
    public String base(Model model) {
        return "base";
    }
    
    @GetMapping("/Services")
    public String services(Model model) {
        return "services";
    }
    
    @GetMapping("/Contact")
    public String contact(Model model) {
        return "contact";
    }
    @GetMapping("/home")
    public String home(Model model) {
        return "home";
    }
    
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
@GetMapping("/register")
public String register(Model model) {
    model.addAttribute("userForm", new userForm());
    return "register";
}



     


    @PostMapping("/do-register")
    public String postMethodName(@Valid @ModelAttribute userForm uzerForm, BindingResult Br,  HttpSession session, RedirectAttributes redirectAttributes) {
        // System.out.println("Processing registration");
        // System.out.println(userForm);
        User u1;
        u1 = User.builder()
                .name(uzerForm.getName())
                .email(uzerForm.getEmail())
                .password(uzerForm.getPassword())
                .About(uzerForm.getAbout())
                .phone(uzerForm.getPhone())
                .profilePic("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.independent.co.uk%2Fsport%2Fcricket%2Falastair-cook-england-joe-root-india-london-b2276403.html&psig=AOvVaw3TYe2k2vignudCDkeNx8wX&ust=1754680128521000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCMCWw4iz-Y4DFQAAAAAdAAAAABAE")
                .provider(SELF)
                .build();

       
     //  User savedUser =  userService.saveUser(user);

     // Validation check
        if (Br.hasErrors()) {
            System.out.println("Validation errors occurred");
            return "register";
        }

        // Save user
        try {
            userService.saveUser(u1);
        } catch (Exception e) {
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
            msg m = msg.builder()
                    .content("Error saving user: " + e.getMessage())
                    .type(msgType.red)
                    .build();
            redirectAttributes.addFlashAttribute("msg", m);
            return "redirect:/register";
        }

        // Remove any previous messages from session
        SessionHelper.removeMessage();

       // msg = "User saved successfully";
      msg m =  msg.builder()
            .content("User registered successfully")
            .type(msgType.green)
            .build();
        redirectAttributes.addFlashAttribute("msg", m);
        return "redirect:/register";
    }
    


    @GetMapping("/forgot-password")
        public String showForgotPasswordPage() {
        return "ForgotPassword"; // forgot-password.html template
    }

   @PostMapping("/forgot-password")
    public String forgotFormOutput(@RequestParam("OTPemail") String OTPemail, Model model) {
        String email = OTPemail;

        User user = userService.getUserByEmail(email);
        if (user == null) {
           //model.addAttribute("msg", "❌ Email not found. Please try again.");
            model.addAttribute("msg", new msg("❌ Email not found.", msgType.red));
            return "ForgotPassword"; // stay on same page
        }

        try {
            otpService.sendOTPEmail(email);
          // model.addAttribute("msg", new msg("✅ OTP sent", msgType.green));
            model.addAttribute("msg", new msg("✅ OTP sent successfully!", msgType.green));
            return "OTP"; // go to OTP entry page

        } catch (Exception e) {
            // Graceful fallback if email fails
            model.addAttribute("msg", new msg("⚠️ Could not send OTP right now. Please try again later.", msgType.red));
            return "ForgotPassword"; // stay on forgot page instead of error 500
        }
    }

    @PostMapping("/verify-otp")
    public String verifyOTP(@RequestParam("otp") int otp, Model model) {
        User user = userService.findByOTP(otp);

        if (user != null && otp == user.getOTPs()) {
            model.addAttribute("msg", new msg("✅ OTP verified. Please set your new password.", msgType.green));
            return "setNewPassword"; // set-new-password.html
        } else {
            model.addAttribute("msg", new msg("❌ Invalid OTP. Please try again.", msgType.red));
            return "OTP";
        }
    }

   @PostMapping("/set-new-password")
    public String setNewPassword(@RequestParam("password") String password,
                                 @RequestParam("email") String email,
                                 Model model) {
        userService.updatePassword(password, email);
        model.addAttribute("msg", new msg("✅ Password updated successfully. Please login.", msgType.green));
        return "login"; // login.html
    }

}
