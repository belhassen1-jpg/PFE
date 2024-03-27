package com.example.pidev.Services;

import com.example.pidev.Entities.*;
import com.example.pidev.Repositories.ConventionRepository;
import com.example.pidev.Repositories.DemandeParticipationConventionRepository;
import com.example.pidev.Repositories.EmployeRepository;
import com.example.pidev.Repositories.PartenaireRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class GestionConventionService {
    @Autowired
    private ConventionRepository conventionRepository;
    @Autowired
    private PartenaireRepository partenaireRepository;
    @Autowired
    private DemandeParticipationConventionRepository demandeRepository;
    @Autowired
    private EmployeRepository employeRepository;

    // Créer ou mettre à jour une convention et l'associer à un partenaire
    public Convention saveOrUpdateConvention(Convention convention, Long partenaireId) {
        Partenaire partenaire = partenaireRepository.findById(partenaireId)
                .orElseThrow(() -> new EntityNotFoundException("Partenaire not found"));
        convention.setPartenaire(partenaire);
        return conventionRepository.save(convention);
    }

    public DemandeParticipationConvention creerDemandeParticipation(Long employeId, Long conventionId) {
        Employe employe = employeRepository.findById(employeId).orElseThrow(() -> new EntityNotFoundException("Employé non trouvé"));
        Convention convention = conventionRepository.findById(conventionId).orElseThrow(() -> new EntityNotFoundException("Convention non trouvée"));

        DemandeParticipationConvention demande = new DemandeParticipationConvention();
        demande.setEmploye(employe);
        demande.setConvention(convention);
        return demandeRepository.save(demande);
    }

    public DemandeParticipationConvention validerDemandeParticipation(Long demandeId) {
        DemandeParticipationConvention demande = demandeRepository.findById(demandeId).orElseThrow(() -> new EntityNotFoundException("Demande non trouvée"));
        demande.setEstValidee(true);
        return demandeRepository.save(demande);
    }


    public List<Convention> trouverConventionsEtParticipants() {
        return conventionRepository.findAll();
    }


    public List<Convention> trouverConventionsParPartenaire(Long partenaireId) {
        return conventionRepository.findByPartenaireId(partenaireId);
    }

    public List<DemandeParticipationConvention> trouverEmployesParConvention(Long conventionId) {
        return demandeRepository.findByConventionIdAndEstValidee(conventionId, true);
    }
    public void retirerEmployeDesConventions(Long empId) {
        // Rechercher toutes les demandes de participation de l'employé à des conventions
        List<DemandeParticipationConvention> demandes = demandeRepository.findByEmployeId(empId);

        // Parcourir chaque demande et la supprimer
        for (DemandeParticipationConvention demande : demandes) {
            demandeRepository.delete(demande);
        }
    }

    public void retirerEmployeDeLConvention(Long empId, Long conventionId) {
        // Recherche de toutes les demandes de l'employé pour cette convention spécifique
        List<DemandeParticipationConvention> demandes = demandeRepository.findByEmployeIdAndConventionId(empId, conventionId);

        // Pour chaque demande trouvée, mettre à jour le statut ou supprimer la demande
        for (DemandeParticipationConvention demande : demandes) {
            // Si vous souhaitez simplement invalider la demande
            demande.setEstValidee(false);
            // Ou si vous souhaitez supprimer la demande
            // demandeParticipationRepository.delete(demande);
        }
        // Sauvegardez les modifications des demandes
        demandeRepository.saveAll(demandes);
    }

}
