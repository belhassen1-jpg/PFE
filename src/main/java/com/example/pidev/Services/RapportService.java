package com.example.pidev.Services;

import com.example.pidev.Entities.*;
import com.example.pidev.Repositories.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class RapportService {
    @Autowired
    private RapportRepository rapportRepository;
    @Autowired
    private EmployeRepository employeRepository;
    @Autowired
    private PlanningService planningService;
    @Autowired
    private DemandeCongeService demandeCongeService;
    @Autowired
    private SessionFormationService sessionFormationService;
    @Autowired
    private GestionEvenementService gestionEvenementService;
    @Autowired
    private PaieRepository paieRepository;
    @Autowired
    private EvaluationPerformanceRepository evaluationPerformanceRepository;

    @Autowired
    private BulletinPaieRepository bulletinPaieRepository;

    public Rapport genererOuMettreAJourRapport(Long employeId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'id : " + employeId));

        Rapport rapport = rapportRepository.findByEmployeEmpId(employeId)
                .orElse(new Rapport());

        // Ici, appeler les méthodes nécessaires pour calculer chaque champ
        rapport.setEmploye(employe);
        rapport.setTotalHeuresTravaillees(planningService.calculerTotalHeuresTravailleesPourEmploye(employeId));
        rapport.setTotalJoursConges(demandeCongeService.calculerTotalJoursCongesPourEmploye(employeId));
        rapport.setNombreFormationsParticipees(sessionFormationService.compterFormationsParticipeesParEmploye(employeId));
        rapport.setNombreEvenementsParticipes(gestionEvenementService.compterEvenementsParticipesParEmploye(employeId));

        Optional<EvaluationPerformance> derniereEvaluation = evaluationPerformanceRepository.findTopByEmployeEmpIdOrderByAnneeDesc(employeId);
        if (derniereEvaluation.isPresent()) {
            rapport.setEvaluationPerformanceRecente(derniereEvaluation.get().getCommentaire());
        }

        Optional<BulletinPaie> dernierBulletinPaie = bulletinPaieRepository.findTopByEmployeIdOrderByDateEmissionDesc(employeId);
        if (dernierBulletinPaie.isPresent()) {
            rapport.setDernierSalaireBrut(dernierBulletinPaie.get().getSalaireBrut());
            rapport.setDernierSalaireNet(dernierBulletinPaie.get().getSalaireNet());
        }
        rapport.setDateRapport(new Date());
        return rapportRepository.save(rapport);
    }
}
