package com.example.pidev.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.prefs.Preferences;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table( name = "Users",uniqueConstraints = { @UniqueConstraint(columnNames = "username"), @UniqueConstraint(columnNames = "email") })
public class User implements Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iduser", length = 255)
    private Long idUser;



    @NotBlank(message = "Username is required")
    @Size(max = 20)
    @NotNull
    private String  Username;

    @NotNull
    private String Password;
    @Email(message = "Email is not valid")
    private String Email;
    private String FirstName;
    private String LastName;
    private String Address;
    private Date BirthDate;
    private Long Phone;
    private Long CIN;
    private Integer IsVerified;
    private String verificationToken;
    private String verificationCode;
    private Integer Age;
    private  LocalDateTime createdAT;

    private String preferencesCategory; // Renamed from preferredCategories
    private String preferencesLocation;

    @PrePersist
    protected void onCreate() {
        this.createdAT = LocalDateTime.now();
    }


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Users_Role",
            joinColumns = @JoinColumn(name = "iduser"),
            inverseJoinColumns = @JoinColumn(name = "idRole"))

    private Set<Role> Roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Employe employe;


    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplication> jobApplications = new ArrayList<>();



}


