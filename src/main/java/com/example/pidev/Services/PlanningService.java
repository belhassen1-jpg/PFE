package com.example.pidev.Services;

import com.example.pidev.Entities.Employe;
import com.example.pidev.Entities.FeuilleTemps;
import com.example.pidev.Entities.Planning;
import com.example.pidev.Repositories.EmployeRepository;
import com.example.pidev.Repositories.FeuilleTempsRepository;
import com.example.pidev.Repositories.PlanningRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class PlanningService {
    @Autowired
    private PlanningRepository planningRepository;
    @Autowired
    private FeuilleTempsRepository feuilleTempsRepository;
    @Autowired
    private EmployeRepository employeRepository;



    public Planning creerPlanningPourEmploye(Long employeId, Planning planning) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new EntityNotFoundException("Employé not found with id: " + employeId));

        planning.setEmploye(employe);
        return planningRepository.save(planning);
    }

    public FeuilleTemps creerEtAssocierFeuilleTemps(Long employeId, Long planningId, FeuilleTemps feuilleTemps) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new EntityNotFoundException("Employé not found with id: " + employeId));
        Planning planning = planningRepository.findById(planningId)
                .orElseThrow(() -> new EntityNotFoundException("Planning not found with id: " + planningId));

        feuilleTemps.setEmploye(employe);
        feuilleTemps.setPlanning(planning);

        return feuilleTempsRepository.save(feuilleTemps);
    }

    public Planning obtenirPlanning(Long id) {
        return planningRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Planning non trouvé"));
    }





    // Utilisez cette méthode pour obtenir les feuilles de temps d'un planning ou d'un employé
    public List<FeuilleTemps> obtenirFeuillesDeTemps(Long planningId, Long employeId) {
        if(planningId != null){
            return feuilleTempsRepository.findByPlanningId(planningId);
        } else if(employeId != null){
            return feuilleTempsRepository.findByEmployeId(employeId);
        } else {
            throw new IllegalArgumentException("Au moins un des paramètres (planningId ou employeId) est requis.");
        }
    }

    // Cette méthode approuve ou rejette une feuille de temps
    public FeuilleTemps changerApprobationFeuilleTemps(Long feuilleTempsId, boolean estApprouve) {
        FeuilleTemps feuilleTemps = feuilleTempsRepository.findById(feuilleTempsId)
                .orElseThrow(() -> new EntityNotFoundException("Feuille de temps non trouvée"));
        feuilleTemps.setEstApprouve(estApprouve);
        return feuilleTempsRepository.save(feuilleTemps);
    }

    public Long calculerHeuresTravaillees(FeuilleTemps feuilleTemps) {
        long heuresTravaillees = 0;
        if (feuilleTemps.getHeureDebut() != null && feuilleTemps.getHeureFin() != null) {
            // Calcul de la différence en millisecondes
            long diff = feuilleTemps.getHeureFin().getTime() - feuilleTemps.getHeureDebut().getTime();
            // Conversion en heures
            heuresTravaillees = diff / (60 * 60 * 1000) % 24;
        }
        return heuresTravaillees;
    }

    public Long calculerTotalHeuresTravailleesPourEmploye(Long employeId) {
        List<FeuilleTemps> feuilles = feuilleTempsRepository.findByEmployeId(employeId);
        long totalHeures = 0;
        for (FeuilleTemps feuille : feuilles) {
            totalHeures += calculerHeuresTravaillees(feuille);
        }
        return totalHeures;
    }


    public List<FeuilleTemps> obtenirFeuillesDeTempsPourPeriode(Long employeId, Date dateDebut, Date dateFin) {
        if (employeId == null || dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("L'ID de l'employé et les dates de début et de fin sont requis.");
        }

        // Ici, vous pouvez ajuster la logique pour filtrer les feuilles de temps entre dateDebut et dateFin
        List<FeuilleTemps> feuilles = feuilleTempsRepository.findByEmployeIdAndPeriode(employeId, dateDebut, dateFin);
        if (feuilles.isEmpty()) {
            throw new EntityNotFoundException("Aucune feuille de temps trouvée pour l'employé avec l'ID " + employeId
                    + " pour la période spécifiée.");
        }

        return feuilles;
    }
}
