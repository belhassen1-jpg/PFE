package com.example.pidev.Services;

import com.example.pidev.Entities.*;
import com.example.pidev.Repositories.DemandeCongeRepository;
import com.example.pidev.Repositories.EmployeRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
@NoArgsConstructor
public class DemandeCongeService {
    @Autowired
    private DemandeCongeRepository demandeCongeRepository;
    @Autowired
    private  EmployeRepository employeRepository;

    private final int nombreMinimalEmployes = 3;

    public List<DemandeConge> getAllDemandesConge() {
        return demandeCongeRepository.findAll();
    }

    public DemandeConge getDemandeConge(Long id) {
        return demandeCongeRepository.findById(id).orElse(null);
    }

    public DemandeConge saveDemandeConge(DemandeConge demandeConge) {
        return demandeCongeRepository.save(demandeConge);
    }

    public void deleteDemandeConge(Long id) {
        demandeCongeRepository.deleteById(id);
    }



    public DemandeConge creerDemandeConge(DemandeConge demande) {
        validateDemandeConge(demande);
        return demandeCongeRepository.save(demande);
    }
    private void validateDemandeConge(DemandeConge demande) {
        if (demande.getEmploye() == null) {
            throw new IllegalArgumentException("La demande doit être associée à un employé.");
        }
        if (demande.getDateDebut().after(demande.getDateFin())) {
            throw new IllegalArgumentException("La date de début doit précéder la date de fin.");
        }
        if (demandeCongeRepository.existsByEmployeAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
                demande.getEmploye(), demande.getDateFin(), demande.getDateDebut())) {
            throw new IllegalStateException("Un congé est déjà enregistré sur cette période pour cet employé.");
        }

    }

    public DemandeConge approuverDemandeConge(Long id, StatutDemande statutDesire) {
        DemandeConge demande = demandeCongeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande de congé non trouvée."));

        // Récupérer l'employé associé à la demande de congé
        Employe employe = employeRepository.findById(demande.getEmploye().getEmpId())
                .orElseThrow(() -> new RuntimeException("Employé non trouvé."));

        if (isDemandeApprouvable(demande, employe)) {
            demande.setStatut(statutDesire);
            return demandeCongeRepository.save(demande);
        } else {
            throw new RuntimeException("La demande ne peut pas être approuvée en raison des politiques du département.");
        }
    }
    private boolean isDemandeApprouvable(DemandeConge demande, Employe employe) {
        // Obtenir le nombre d'employés dans le département de l'employé
        long nombreEmployesDepartement = employeRepository.countByDepartement(employe.getDepartement());
        System.out.println("Nombre d'employés dans le département : " + nombreEmployesDepartement);

        // Obtenir le nombre d'employés en congé dans le même département pendant la période
        long nombreEmployesEnConge = demandeCongeRepository.countByDepartementAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
                employe.getDepartement(), demande.getDateDebut(), demande.getDateFin());
        System.out.println("Nombre d'employés en congé pendant la période : " + nombreEmployesEnConge);

        // Déterminer si le département peut fonctionner avec le nombre restant d'employés
        boolean approvable = (nombreEmployesDepartement - nombreEmployesEnConge) > nombreMinimalEmployes;
        System.out.println("La demande est-elle approuvable ? " + approvable);
        return approvable;
    }


    public Long calculerDureeTotaleConge(DemandeConge demandeConge) {
        long duree = 0;
        if (demandeConge.getDateDebut() != null && demandeConge.getDateFin() != null) {
            // Calcul de la différence en millisecondes
            long diff = demandeConge.getDateFin().getTime() - demandeConge.getDateDebut().getTime();
            // Conversion en jours
            duree = diff / (24 * 60 * 60 * 1000);
        }
        return duree;
    }

    public Long calculerTotalJoursCongesPourEmploye(Long employeId) {
        List<DemandeConge> demandes = demandeCongeRepository.findByEmployeId(employeId);
        long totalJours = 0;
        for (DemandeConge demande : demandes) {
            totalJours += calculerDureeTotaleConge(demande);
        }
        return totalJours;
    }
}
