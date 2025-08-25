package com.example.scm.Entities;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    @Id
    private String id;
    @NotBlank(message = "Name is required")
    private String name;
    private String email;
    private String phone;
    private String address;
    private String picture;
    @Column(length = 1000)
    private String description;
    private boolean favorite = false;
    private String websiteLink;
    private String linkedinLink;

    


    private String cloudinaryIMGPublicId;

    // private List<String> links = new ArrayList<>();
    @ManyToOne
    @JsonIgnore
    private User user;

     @OneToMany(mappedBy = "contact", cascade= CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true)
    private List<SocilaLinks> socialLinks = new ArrayList<>();

}
