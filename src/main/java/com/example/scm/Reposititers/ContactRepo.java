package com.example.scm.Reposititers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.scm.Entities.Contact;
import com.example.scm.Entities.User;

@Repository
public interface ContactRepo extends JpaRepository<Contact, String>{


    // find the contact my user
    Page<Contact> findByUser(User user, Pageable pageable);

    // custom query methods
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

    Page<Contact> findByUserAndNameContaining(User user,String namekeyword,  Pageable pageable);
    Page<Contact> findByUserAndEmailContaining(User user,String emailkeyword, Pageable pageable);
    Page<Contact> findByUserAndPhoneContaining(User user,String phonekeyword, Pageable pageable);



    // delete by id
    void deleteById(String id);

}
