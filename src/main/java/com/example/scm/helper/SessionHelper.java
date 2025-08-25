package com.example.scm.helper;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Component
public class SessionHelper {

    public static void removeMessage(){
       try {
        System.out.println("Removing message from session");
        HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        session.removeAttribute("msg");
       } catch (Exception e) {
        System.err.println("Error removing message from session: " + e);
           e.printStackTrace();
           }
    }
}
