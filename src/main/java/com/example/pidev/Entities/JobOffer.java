package com.example.pidev.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class JobOffer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Job title is required")
    private String title;
    private String description;
    private Double salary;
    private String location;
    private Integer requiredExperienceYears;
    private String projectDetails;
    private String category;



    @OneToMany(mappedBy = "jobOffer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<JobApplication> applications = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "job_offer_keywords", joinColumns = @JoinColumn(name = "job_offer_id"))
    private Set<String> keywords = new HashSet<>(); // Liste des mots-clés associés à l'offre d'emploi

}
