package com.example.scm.Form;

import org.springframework.web.multipart.MultipartFile;

import com.example.scm.Vaidator.MaxImageSize;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Contact_form {

@NotBlank(message = "Name is required")
private String name;

@Email(message = "Email is required")
@NotBlank(message = "Email is required")
private String email;

@Pattern(regexp = "^\\+?[0-9. ()-]{7,}$", message = "Phone number is required")
private String phoneNumber;

@NotBlank(message = "Address is required")
private String address;
 

private String description;

private String websiteLink;
private String linkedinLink;
private Boolean favorite;

// annotation create krege jo file ko validate krega
@MaxImageSize(value = 2048, message = "Image must be less than 0.5 MB")
private MultipartFile ContactIMG;

private String picture;




}
