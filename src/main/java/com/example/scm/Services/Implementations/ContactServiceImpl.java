package com.example.scm.Services.Implementations;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.scm.Entities.Contact;
import com.example.scm.Entities.User;
import com.example.scm.Reposititers.ContactRepo;
import com.example.scm.Reposititers.UserRepo;
import com.example.scm.Services.DefinationFolder.ContactServices;

@Service
public class ContactServiceImpl implements ContactServices  {

    @Autowired
    UserRepo userRepo;
    @Autowired
    private ContactRepo contactRepo;

     private  Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Override
    public Contact saveContact(Contact contact) {
        
        // Generate a unique ID for the contact
        String id = UUID.randomUUID().toString();
        contact.setId(id);
        
        // Save the contact to the repository
        return contactRepo.save(contact);
    }

    @Override
    public Contact updateContact(Contact contact) {

      var OldContact = contactRepo.findById(contact.getId()).orElseThrow(() -> new RuntimeException("Contact not found"));
        OldContact.setName(contact.getName());
        OldContact.setEmail(contact.getEmail());
        OldContact.setPhone(contact.getPhone());
        OldContact.setAddress(contact.getAddress());
        OldContact.setFavorite(contact.isFavorite());
        OldContact.setDescription(contact.getDescription());
        OldContact.setPicture(contact.getPicture());
        OldContact.setWebsiteLink(contact.getWebsiteLink());
        OldContact.setLinkedinLink(contact.getLinkedinLink());
        OldContact.setCloudinaryIMGPublicId(contact.getCloudinaryIMGPublicId());
        

        // Save the updated contact
        return contactRepo.save(OldContact);
    }

    @Override
    public Contact getContactById(String id) {
        return contactRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepo.findAll();
    }

   
    @Override
    @Transactional
    public void deleteContact(String id) {
        var contact = contactRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        User user = contact.getUser();              // parent user nikal
        user.getContacts().remove(contact);         // list se remove kar
        contact.setUser(null);                      // relation tod de
        userRepo.save(user);                        // save kar
    }


    

    @Override
    public List<Contact> getContactsByUserId(String userId) {
      return contactRepo.findByUserId(userId);
    }

    @Override
    public Page<Contact> getContactsByUser(User user, int page, int size , String sortBy,String direction) {


        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUser(user, pageable);
    }

    @Override
    public Page<Contact> searchByName(User user,String namekeyword,int size, int page, String sortBy, String order) {
         Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
         var pageable = PageRequest.of(page, size, sort);
         return contactRepo.findByUserAndNameContaining(user,namekeyword, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(User user,String emailkeyword, int size, int page, String sortBy,
            String order) {
         Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
         var pageable = PageRequest.of(page, size, sort);
         return contactRepo.findByUserAndEmailContaining(user,emailkeyword, pageable);
    }

    @Override
    public Page<Contact> searchByPhone(User user,String phonekeyword,  int size, int page, String sortBy,
            String order) {
         Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
         var pageable = PageRequest.of(page, size, sort);
         return contactRepo.findByUserAndPhoneContaining(user,phonekeyword,  pageable);
    }




    

    
}
