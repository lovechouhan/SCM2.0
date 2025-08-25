package com.example.scm.Entities;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString



public class User implements UserDetails{
    @Id
    private String userid;
    @Column(nullable = false)
    private String name;
    private String email;
     @Getter(value = AccessLevel.NONE)  // -> ab ye password ka getter nahi banega
    private String password;
    @Column(length=1000)
    private String About;
    @Column(length=1000)
    private String profilePic;
    private String phone;

   // @Getter(value = AccessLevel.NONE)  // -> ab ye enabled ka getter nahi banega
    private boolean enabled = true;


    private boolean EmailVerified = false;
    private boolean phoneVerified = false;

    private String emailToken;

    @Enumerated(value = EnumType.STRING)
    private Providers provider = Providers.SELF;
    private String providerId;

    @OneToMany(mappedBy = "user", cascade= CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true)
    private List<Contact> contacts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // list of roles[USER, ADMIN]
        // Collection of SimpleGrantedAuthority[roles{ADMIN,USER}]
        // list return krega
        // uss list ke andr SimpleGrantedAuthority hogi
        // aur simpleGrantedAuthority ke andar roles name hoga
        Collection<SimpleGrantedAuthority> roles = roleList.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
       // throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");\
       return roles;
    }

    // for this project 
    // hamara email id hi username hai
    @Override
    public String getUsername() {
       return this.email;
       // throw new UnsupportedOperationException("Unimplemented method 'getUsername'");
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    // indicates if the user is enabled or not . A disabled user cannot authenticated
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getPassword() {
        return this.password;
        //throw new UnsupportedOperationException("Unimplemented method 'getPassword'");
    }





   
}

