package com.example.scm.Config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.scm.Services.Implementations.SecurityCustomUserDetailsService;



@Configuration
public class SecurityConfig {

   
    @Autowired
    private SecurityCustomUserDetailsService customUserDetailsService;
    
   @Autowired
   private OAuthenticationSuccessHandler handler;

   @Autowired
   private AuthFailureHandler authFailureHandler;


    // Configuration of Authentication Provider for spring security
    // DB se user ko extract krna hain
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // user detail service ka object paas karna hai
        provider.setUserDetailsService(customUserDetailsService);
        // password encoder ka object set karna hai
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    // configure your security filter chain here
    // user konse pages and url configure kr payega
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception  {
        // configure the http security and url 
        // url configure lkiya hain ki kaun se public rahege aur kaun se private rahege
        httpSecurity.authorizeHttpRequests(authorize-> {
 
            
         // authenticate all activities started with users
            authorize.requestMatchers("/user/**").authenticated();
            authorize.requestMatchers("/oauth2/**").permitAll(); // allow oauth2 login without authentication
            authorize.anyRequest().permitAll(); //  all remining pages  are accessible without authentication
        });

        // default form login
        // agar hame kuch bhi change krna hua to hum yaha ayenge : form login
        httpSecurity.formLogin(formLogin->{
            formLogin.loginPage("/login") // custom login page
                    .loginProcessingUrl("/authenticate") // form action url
                    .defaultSuccessUrl("/user/dashboard", true) // after successful login redirect to home page
                    .failureForwardUrl("/login?error=true") // if login fails, redirect to login page with error
                    .usernameParameter("email") // form field name for username
                    .passwordParameter("password") ;// form field name for password
                    
        
                    
        // we handle authentication failure here
       formLogin.failureHandler(authFailureHandler); // use custom failure handler
            
         
                    
        }); // form login is used for authentication



        // logout configuration
        httpSecurity.csrf(AbstractHttpConfigurer::disable); // disable CSRF for simplicity, not recommended for production


        httpSecurity.logout(logout->{
            logout.logoutUrl("/logout") // logout url
                   .logoutSuccessUrl("/login?logout=true") ;// after successful logout redirect to login page with logout message
                    // .invalidateHttpSession(true) // invalidate the session
                    // .deleteCookies("JSESSIONID"); // delete the session cookie
        });


        // Oauth 2 configuration
        httpSecurity.oauth2Login(oauth->{
            oauth.loginPage("/login");// login page for oauth2
            oauth.successHandler(handler); // use custom success handler
        }); // use default oauth2 login configuration



//         httpSecurity.logout(logout -> {
//     logout.logoutUrl("/logout")
//            .logoutSuccessUrl("/login?logout=true")
//            .invalidateHttpSession(true)
//            .deleteCookies("JSESSIONID");
// });









       return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // NoOpPasswordEncoder is deprecated, use with caution
    }

}
