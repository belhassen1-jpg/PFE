package com.example.pidev.DTO;

import com.example.pidev.Entities.StatutDemande;
import lombok.Data;

@Data
public class JobApplicationDto {
    private Long jobOfferId;
    private Long userId;
    private String applicantName;
    private String applicantEmail;
    private Long applicantPhone;
    private String applicantAddress;
    private Integer yearsOfExperience;
    private String resumePath; // Chemin vers le CV téléchargé
    private String coverLetterPath; // Chemin vers la lettre de motivation téléchargée
    private StatutDemande status; // Ajout du statut de la candidature

}
