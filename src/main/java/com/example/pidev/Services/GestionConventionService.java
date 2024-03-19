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
        // Récupérer l'événement et l'employé à partir de la demande
        Convention convention = demande.getConvention();
        Employe employe = demande.getEmploye();

        // Ajouter l'employé à la liste des participants de l'événement
        Set<Employe> participants = convention.getParticipants();
        participants.add(employe);
        convention.setParticipants(participants);

        // Sauvegarder les modifications
        conventionRepository.save(convention);
        return demandeRepository.save(demande);
    }


    public List<Convention> trouverConventionsEtParticipants() {
        return conventionRepository.findAllWithParticipants();
    }


    public List<Convention> trouverConventionsParPartenaire(Long partenaireId) {
        return conventionRepository.findByPartenaireId(partenaireId);
    }

    public List<DemandeParticipationConvention> trouverEmployesParConvention(Long conventionId) {
        return demandeRepository.findByConventionIdAndEstValidee(conventionId, true);
    }
    public void retirerEmployeDesConventions(Long empId) {
        List<Convention> conventions = conventionRepository.findByParticipantsId(empId);
        for (Convention convention : conventions) {
            convention.getParticipants().removeIf(participant -> participant.getEmpId().equals(empId));
            conventionRepository.save(convention);
        }
    }

    public void retirerEmployeDeLEvenement(Long empId, Long conventionId) {
        Convention convention = conventionRepository.findById(conventionId)
                .orElseThrow(() -> new EntityNotFoundException("convention non trouvé"));
        convention.getParticipants().removeIf(participant -> participant.getEmpId().equals(empId));
        conventionRepository.save(convention);
    }

}
