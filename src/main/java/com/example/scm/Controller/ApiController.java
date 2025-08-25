package com.example.scm.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.scm.Entities.Contact;
import com.example.scm.Services.DefinationFolder.ContactServices;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ContactServices contactService;

    @GetMapping("/contacts/{contactId}")
    public Contact getContacts(@PathVariable String contactId) {
        return contactService.getContactById(contactId);
    }

    // @PostMapping("/contacts")
    // public Contact createContact(@RequestBody Contact contact) {
    //     return contactService.createContact(contact);
    // }

    // @PutMapping("/contacts/{id}")
    // public Contact updateContact(@PathVariable Long id, @RequestBody Contact contact) {
    //     return contactService.updateContact(id, contact);
    // }

    // @DeleteMapping("/contacts/{id}")
    // public void deleteContact(@PathVariable Long id) {
    //     contactService.deleteContact(id);
    // }
}
