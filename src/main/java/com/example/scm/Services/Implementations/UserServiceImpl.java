package com.example.scm.Services.Implementations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.scm.Entities.User;
import com.example.scm.Reposititers.UserRepo;
import com.example.scm.Services.DefinationFolder.EmailService;
import com.example.scm.Services.DefinationFolder.userServices;
import com.example.scm.helper.AppConstants;
import com.example.scm.helper.Helper;
import com.example.scm.helper.ResourceNotFoundException;




@Service

public class UserServiceImpl implements userServices{

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;



    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private EmailService emailService;

    @Override
    public User saveUser(User user) {
       
        String userId = UUID.randomUUID().toString();
        // user id : have to generate
        user.setUserid(userId);
        // passsword : have to encode
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // set the role
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setEnabled(false); // ✅ Default enabled
        logger.info(user.getProvider().toString());
       
        String emailToken = UUID.randomUUID().toString();
        user.setEmailToken(emailToken);
        User savedUser = userRepo.save(user);
        String emailLink = Helper.getLinkForEmailVerification(emailToken);

        emailService.sendEmail(savedUser.getEmail(), "Verify Account : Email Contact Manager", emailLink);

        return savedUser;
    }

    @Override
    public Optional<User> getUserById(String userId) {
        Optional<User> user = userRepo.findById(userId);
        if (user.isPresent()) {
            logger.info("User found with ID: {}", userId);
        } else {
            logger.warn("No user found with ID: {}", userId);
            return Optional.empty();
        }   
        return user;
        //throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
    }

    @Override
    public Optional<User> updateUser(User user) {
         User u1 = userRepo.findById(user.getUserid()).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + user.getUserid()));
        u1.setName(user.getName());
        u1.setEmail(user.getEmail());
        u1.setPassword(passwordEncoder.encode(user.getPassword()));
        u1.setAbout(user.getAbout());
        u1.setPhone(user.getPhone());
        u1.setProfilePic(user.getProfilePic());
        u1.setEnabled(user.isEnabled());
        u1.setEmailVerified(user.isEmailVerified());
        u1.setPhoneVerified(user.isPhoneVerified());
        u1.setProvider(user.getProvider());  

       User saving =  userRepo.save(u1);
       return Optional.ofNullable(saving);
        //throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void deleteUser(String userId) {
       User u1 = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (u1 == null) {
            logger.error("User with ID: {} not found for deletion", userId);
        
        }
       else
       { logger.info("Deleting user with ID: {}", userId);
        // Remove all contacts associated with the user

        userRepo.deleteById(userId);
        logger.info("User with ID: {} has been deleted", userId);}
       // throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public boolean isUserExists(String userId) {
      
        User u3 = userRepo.findById(userId).orElse(null);
        return u3 != null ? true : false;
        // boolean exists = userRepo.existsById(userId);
        // if (exists) {
        //     logger.info("User exists with ID: {}", userId);
        // } else {
        //     logger.warn("User does not exist with ID: {}", userId);
        // }   
        // return exists;
        // throw new UnsupportedOperationException("Unimplemented method 'isUserExists'");
    }

    @Override
    public boolean existsByEmail(String email) {
        User user = userRepo.findByEmail(email).orElse(null);
        return user != null ? true : false;
        //  boolean exists  = userRepo.existsByEmail(email);
        // if (exists) {
        //     logger.info("User exists with email: {}", email);
        // } else {
        //     logger.warn("User does not exist with email: {}", email);
        // }
        // return exists;
      //  throw new UnsupportedOperationException("Unimplemented method 'existsByEmail'");
    }

    @Override
    public List<User> getAllUsers() {
       
        List<User> users = userRepo.findAll();
        if (users.isEmpty()) {
            logger.warn("No users found in the database");
        } else {
            logger.info("Retrieved {} users from the database", users.size());
        }
        return users;
       // throw new UnsupportedOperationException("Unimplemented method 'getAllUsers'");
    }

    @Override
    public User getUserByEmail(String email) {
        
        return userRepo.findByEmail(email).orElse(null);
    }

    @Override
    public User findByEmailToken(String token) {
        User user = userRepo.findByEmailToken(token).orElse(null);
        if (user != null) {
            logger.info("User found with email token: {}", token);
        } else {
            logger.warn("No user found with email token: {}", token);
        }
        return user;
    }

}
