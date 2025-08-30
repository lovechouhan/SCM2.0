package com.example.scm.Reposititers;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.scm.Entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

   
    boolean existsByEmail(String email);
    // Additional query methods can be defined here if needed
    // For example, to find a user by email:
    // Optional<User> findByEmail(String email);

    
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailToken(String token);


    @Query("SELECT u FROM User u WHERE u.OTPs = ?1")
    Optional<User> findByOTPToken(int otp);


}
