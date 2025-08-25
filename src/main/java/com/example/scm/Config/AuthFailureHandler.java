package com.example.scm.Config;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.example.scm.helper.msg;
import com.example.scm.helper.msgType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthFailureHandler implements AuthenticationFailureHandler {

    // This method will be called when authentication fails
    // we can also Anonymous class or lamba exprsiion method here
    // we make a differetn class to handle auth failure
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

                HttpSession session = request.getSession();
              // session.setAttribute("mssg", msg.builder().content("User is disabled").type(msgType.red).build());
                // if user is Disabled(Not Verified)
                if(exception instanceof DisabledException){
        session.setAttribute("mssg", "User is disabled, Email with Verification Link is Sent on your Email ID");
    } else if(exception instanceof BadCredentialsException){
        session.setAttribute("mssg", "Invalid username or password");
    } else {
        session.setAttribute("mssg", "Authentication failed: " + exception.getMessage());
    }

                  // store in session
        

        // redirect back to login
        response.sendRedirect("/login");

    }

}
