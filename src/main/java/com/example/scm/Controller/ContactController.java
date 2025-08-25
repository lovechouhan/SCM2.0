package com.example.scm.Controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.scm.Entities.Contact;
import com.example.scm.Entities.User;
import com.example.scm.Form.ContactSearchForm;
import com.example.scm.Form.Contact_form;
import com.example.scm.Services.DefinationFolder.ContactServices;
import com.example.scm.Services.DefinationFolder.ImageService;
import com.example.scm.Services.DefinationFolder.userServices;
import com.example.scm.helper.AppConstants;
import com.example.scm.helper.Helper;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/user/contact")
public class ContactController {

    private  Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private  ContactServices contactService;

    @Autowired
    private  userServices uzerService;

    @Autowired
    private ImageService imageService;

    @RequestMapping("/add")
    public String addContactView(Model model) {
        Contact_form contactForm = new Contact_form();
        

        contactForm.setFavorite(true);
        model.addAttribute("contactForm", contactForm);
        return "users/add_contact";
    }

    @PostMapping("/add")
    public String savingContacts(@Valid @ModelAttribute("contactForm") Contact_form contactForm, BindingResult result, Authentication auth) {
     
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> {
                logger.error("Validation error: " + error.getDefaultMessage());
            });
            // If there are validation errors, return to the form view
            return "users/add_contact";
        }


        // process the form data
        String username = Helper.getEmailofLoggedInUser(auth);


        // form ke data se Contact  banate hain
       
        User user = uzerService.getUserByEmail(username);

        // image process
       logger.info("Processing image for contact: " + contactForm.getContactIMG().getOriginalFilename());

       // Here we add the code to process the image and save it

       String filename = UUID.randomUUID().toString();
       String fileURL ;

        if (contactForm.getContactIMG() == null || contactForm.getContactIMG().isEmpty()) {
        // Set default image URL if no image uploaded
        fileURL = "";
    } else {
        logger.info("Processing image for contact: " + contactForm.getContactIMG().getOriginalFilename());
        fileURL = imageService.uploadImage(contactForm.getContactIMG(), filename);
    }

        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhone(contactForm.getPhoneNumber());
        contact.setFavorite(contactForm.getFavorite());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setCloudinaryIMGPublicId(filename); // Set the public ID for the image
        contact.setPicture(fileURL); // Set the image URL from the uploaded image
        contact.setWebsiteLink(contactForm.getWebsiteLink());
      
        contact.setLinkedinLink(contactForm.getLinkedinLink());

        contactService.saveContact(contact);

       

        System.out.println(contactForm);



        return "redirect:/user/contact/add";
    }

    @RequestMapping
    public String viewContacts(
        @RequestParam(value ="page", defaultValue = "0") int page,
        @RequestParam(value ="size", defaultValue = "10") int size,
        @RequestParam(value ="sortField", defaultValue = "name") String sortField,
        @RequestParam(value ="sortDirection", defaultValue = "asc") String sortDirection,
        Model model, Authentication auth) {
        String username = Helper.getEmailofLoggedInUser(auth);

        User user = uzerService.getUserByEmail(username);

        Page<Contact> contacts = contactService.getContactsByUser(user, page, size, sortField, sortDirection);

        model.addAttribute("contacts", contacts);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
       model.addAttribute("searchForm", new ContactSearchForm());
       
        return "users/view_contacts";
    }
    
   
    // search handler
    @GetMapping("/search")
    public String searchHandler(
       @ModelAttribute("searchForm") ContactSearchForm searchForm,
        @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model, Authentication auth
    ){
        logger.info("Searching for contacts with field: {} and keyword: '{}'", searchForm.getField(), searchForm.getKeyword());
        Page<Contact> contactsPage = Page.empty();
        var user = uzerService.getUserByEmail(Helper.getEmailofLoggedInUser(auth));

        if (searchForm.getField().equalsIgnoreCase("name")) {
            contactsPage = contactService.searchByName(user, searchForm.getKeyword(), size, page, sortBy, direction);
        } else if (searchForm.getField().equalsIgnoreCase("email")) {
            contactsPage = contactService.searchByEmail(user, searchForm.getKeyword(), size, page, sortBy, direction);
        } else if (searchForm.getField().equalsIgnoreCase("phone")) {
            contactsPage = contactService.searchByPhone(user, searchForm.getKeyword(), size, page, sortBy, direction);
        } 
        logger.info("contactsPage: {}", contactsPage);
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("contacts", contactsPage);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        return "users/search";
    }
  
    // deleting contact
    // contactId ek variable hai apni marzi se likh sakte hoo
    @GetMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable String contactId, HttpSession session) {
        contactService.deleteContact(contactId);
        logger.info("Contact with ID {} deleted successfully", contactId);

        // Use a simple string message or create a custom message object if needed
        session.setAttribute("message", "Contact deleted successfully");
        return "redirect:/user/contact";
    }

    // updating contact handler
    @GetMapping("/view/{contactId}")
    public String updateContactFormView( @PathVariable String contactId, Model model) {

        // if(result.hasErrors()) {
        //     logger.error("Validation errors found while updating contact: {}", result.getAllErrors());
        //    model.addAttribute("contactForm", new Contact_form());
        //     return "users/update_contact";
        // }

        var contact = contactService.getContactById(contactId);
        Contact_form contactForm = new Contact_form();
        
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhone());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavorite(contact.isFavorite());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedinLink(contact.getLinkedinLink());
        contactForm.setPicture(contact.getPicture());


        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);
        return "users/update_contact";
    }

    @PostMapping("/update/{contactId}")
    public String updateContact(@PathVariable String contactId,@Valid @ModelAttribute("contactForm") Contact_form contactForm, BindingResult result, @RequestParam("ContactIMG") MultipartFile file, Model model) {
      
        if (result.hasErrors()) {
            logger.error("Validation errors found while updating contact: {}", result.getAllErrors());
            model.addAttribute("contactId", contactId);
            return "users/update_contact";
        }
        //var con = new Contact(); // make a new contact
        var con = contactService.getContactById(contactId); //using existing contact instead of making a new one for updating details
        con.setId(contactId);
        con.setName(contactForm.getName());
        con.setEmail(contactForm.getEmail());
        con.setPhone(contactForm.getPhoneNumber());
        con.setAddress(contactForm.getAddress());
        con.setDescription(contactForm.getDescription());
        con.setFavorite(contactForm.getFavorite());
        con.setWebsiteLink(contactForm.getWebsiteLink());
        con.setLinkedinLink(contactForm.getLinkedinLink());
        

        // process Image
       if (contactForm.getContactIMG() != null && !contactForm.getContactIMG().isEmpty()) {
            // Handle image processing
            logger.info("file is not empty");
            String filename = UUID.randomUUID().toString();
            String imgURL = imageService.uploadImage(contactForm.getContactIMG(), filename);
            con.setCloudinaryIMGPublicId(filename); // Set the public ID for the image
            con.setPicture(imgURL); // Set the image URL
            contactForm.setPicture(imgURL); // Update the contactForm with the new image URL
        } else{
            logger.info("file is empty");
        }

       var updatedContact = contactService.updateContact(con);
       logger.info("Contact updated successfully: {}", updatedContact);
        return "redirect:/user/contact/view/" + contactId;
    }

   


}
