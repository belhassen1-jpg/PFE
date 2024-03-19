package com.example.pidev.Services;

import com.example.pidev.Entities.DemandeParticipationEvenement;
import com.example.pidev.Entities.Employe;
import com.example.pidev.Entities.Evenement;
import com.example.pidev.Entities.Partenaire;
import com.example.pidev.Repositories.DemandeParticipationEvenementRepository;
import com.example.pidev.Repositories.EmployeRepository;
import com.example.pidev.Repositories.EvenementRepository;
import com.example.pidev.Repositories.PartenaireRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class GestionEvenementService {
  @Autowired
    private EvenementRepository evenementRepository;
    @Autowired
    private PartenaireRepository partenaireRepository;
    @Autowired
    private DemandeParticipationEvenementRepository demandeParticipationEvenementRepository;
    @Autowired
    private EmployeRepository employeRepository;

    // Créer ou mettre à jour un événement et l'associer à un partenaire
    public Evenement creerOuMajEvenement(Evenement evenement, Long partenaireId) {
        Partenaire partenaire = partenaireRepository.findById(partenaireId)
                .orElseThrow(() -> new EntityNotFoundException("Partenaire non trouvé"));

        evenement.setPartenaire(partenaire); // Associe le partenaire à l'événement
        // La logique pour vérifier si c'est une mise à jour ou une création d'un nouvel événement
        return evenementRepository.save(evenement);
    }


    // Créer une demande de participation à un événement
    public DemandeParticipationEvenement creerDemandeParticipation(Long employeId, Long evenementId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new EntityNotFoundException("Employé non trouvé"));
        Evenement evenement = evenementRepository.findById(evenementId)
                .orElseThrow(() -> new EntityNotFoundException("Événement non trouvé"));

        DemandeParticipationEvenement demande = new DemandeParticipationEvenement();
        demande.setEmploye(employe);
        demande.setEvenement(evenement);
        return demandeParticipationEvenementRepository.save(demande);
    }

    // Valider une demande de participation à un événement
    public DemandeParticipationEvenement validerDemandeParticipation(Long demandeId) {
        DemandeParticipationEvenement demande = demandeParticipationEvenementRepository.findById(demandeId)
                .orElseThrow(() -> new EntityNotFoundException("Demande non trouvée"));
        demande.setEstValidee(true);

        // Récupérer l'événement et l'employé à partir de la demande
        Evenement evenement = demande.getEvenement();
        Employe employe = demande.getEmploye();

        // Ajouter l'employé à la liste des participants de l'événement
        Set<Employe> participants = evenement.getParticipants();
        participants.add(employe);
        evenement.setParticipants(participants);

        // Sauvegarder les modifications
        evenementRepository.save(evenement);
        return demandeParticipationEvenementRepository.save(demande);
    }

    // Trouver des événements par partenaire
    public List<Evenement> trouverEvenementsParPartenaire(Long partenaireId) {
        return evenementRepository.findByPartenaireId(partenaireId);
    }

    // Trouver les participants d'un événement
    public Set<Employe> trouverParticipantsParEvenement(Long evenementId) {
        Evenement evenement = evenementRepository.findById(evenementId)
                .orElseThrow(() -> new EntityNotFoundException("Événement non trouvé"));
        return evenement.getParticipants();
    }

    // Méthode pour récupérer la liste des événements et leurs participants
    public List<Evenement> trouverEvenementsEtParticipants() {
        return evenementRepository.findAllWithParticipants();
    }

    public void retirerEmployeDesEvenements(Long empId) {
        List<Evenement> evenements = evenementRepository.findByParticipantsId(empId);
        for (Evenement evenement : evenements) {
            evenement.getParticipants().removeIf(participant -> participant.getEmpId().equals(empId));
            evenementRepository.save(evenement);
        }
    }

    public void retirerEmployeDeLEvenement(Long empId, Long evenementId) {
        Evenement evenement = evenementRepository.findById(evenementId)
                .orElseThrow(() -> new EntityNotFoundException("Événement non trouvé"));
        evenement.getParticipants().removeIf(participant -> participant.getEmpId().equals(empId));
        evenementRepository.save(evenement);
    }


    public int compterEvenementsParticipesParEmploye(Long employeId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new EntityNotFoundException("Employé non trouvé avec l'id : " + employeId));

        return (int) evenementRepository.findAll().stream()
                .filter(evenement -> evenement.getParticipants().contains(employe))
                .count();
    }
}
