package com.example.scm.Services.DefinationFolder;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.scm.Entities.Contact;
import com.example.scm.Entities.User;



public interface  ContactServices {

    // save contacts
    Contact saveContact(Contact contactForm);
 
    //update contacts
    Contact updateContact( Contact contactForm);

    // get contacts by id
    Contact getContactById(String id);

    // get all contacts
    List<Contact> getAllContacts();

    // delete contacts
    void deleteContact(String id);

    
    // get contacts by user id
    List<Contact> getContactsByUserId(String userId);

    // get by user
    Page<Contact> getContactsByUser(User userId, int page, int size, String sortField, String sortDirection);

    // searching
    Page<Contact> searchByName(User user,String namekeyword, int size, int page, String sortBy, String order);

    Page<Contact> searchByEmail(User user,String emailkeyword, int size, int page, String sortBy, String order);

    Page<Contact> searchByPhone(User user,String phonekeyword, int size, int page, String sortBy, String order);

}
