package com.example.pidev.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobApplication implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String applicantName;
    private String applicantEmail;
    private Long applicantPhone;
    private String applicantAddress;
    private Integer yearsOfExperience;
    // A resume and a cover letter will be stored as file paths or URLs
    private String resumePath;
    private String coverLetterPath;

    // Link back to the job offer
    @Enumerated(EnumType.STRING)
    private StatutDemande status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_offer_id")
    @JsonBackReference
    private JobOffer jobOffer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User applicant;


}

