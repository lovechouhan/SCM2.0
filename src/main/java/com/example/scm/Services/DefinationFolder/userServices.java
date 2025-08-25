package com.example.scm.Services.DefinationFolder;

import java.util.List;
import java.util.Optional;

import com.example.scm.Entities.User;

public interface  userServices {
    User saveUser(User user);
    Optional<User> getUserById(String userId);
    Optional<User> updateUser(User user);
    void deleteUser(String userId);
    boolean isUserExists(String userId);
    boolean existsByEmail(String email);
    List<User> getAllUsers();
    User getUserByEmail(String email);
    User findByEmailToken(String token);
}
