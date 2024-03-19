package com.example.pidev.Services;

import com.example.pidev.Entities.AnalyseFinanciere;
import com.example.pidev.Entities.Depense;
import com.example.pidev.Entities.Employe;
import com.example.pidev.Entities.ObjectifEpargne;
import com.example.pidev.Repositories.AnalyseFinanciereRepository;
import com.example.pidev.Repositories.DepenseRepository;
import com.example.pidev.Repositories.EmployeRepository;
import com.example.pidev.Repositories.ObjectifEpargneRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ServiceGestionFinanciere {

    @Autowired
    private DepenseRepository depenseRepository;

    @Autowired
    private ObjectifEpargneRepository objectifEpargneRepository;

    @Autowired
    private AnalyseFinanciereRepository analyseFinanciereRepository;

    @Autowired
    private EmployeRepository employeRepository;

    public Depense enregistrerDepense(Long employeId, Depense depense) {
        if (depense.getMontant() == null || depense.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant de la dépense doit être supérieur à zéro.");
        }
        if (depense.getCategorie() == null || depense.getCategorie().isEmpty()) {
            throw new IllegalArgumentException("La catégorie de la dépense est obligatoire.");
        }
        if (depense.getDescription() == null || depense.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description de la dépense est obligatoire.");
        }

        Employe employe = employeRepository.findById(employeId).orElseThrow(
                () -> new RuntimeException("Employé non trouvé avec l'id : " + employeId));

        depense.setEmploye(employe);
        depense.setDateDepense(new Date()); // Utiliser la date de la dépense si elle est fournie
        return depenseRepository.save(depense);
    }

    public ObjectifEpargne definirObjectifEpargne(Long employeId, ObjectifEpargne objectif) {
        if (objectif.getObjectifMontant() == null || objectif.getObjectifMontant().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant de l'objectif doit être supérieur à zéro.");
        }
        if (objectif.getDateDebut() == null) {
            throw new IllegalArgumentException("La date de début est obligatoire.");
        }
        if (objectif.getDateFin() == null || objectif.getDateFin().before(objectif.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin est obligatoire et doit être après la date de début.");
        }

        Employe employe = employeRepository.findById(employeId).orElseThrow(
                () -> new RuntimeException("Employé non trouvé avec l'id : " + employeId));

        objectif.setEmploye(employe);
        return objectifEpargneRepository.save(objectif);
    }

    public AnalyseFinanciere analyserFinances(Long employeId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'id : " + employeId));

        AnalyseFinanciere analyse = new AnalyseFinanciere();
        analyse.setEmploye(employe);
        analyse.setDateAnalyse(new Date());

        BigDecimal depenseMoyenneMensuelle = calculerDepenseMoyenneMensuelle(employeId);
        BigDecimal epargneMoyenneMensuelle = calculerEpargneMoyenneMensuelle(employeId);
        BigDecimal tauxEpargneMensuel = epargneMoyenneMensuelle.divide(depenseMoyenneMensuelle, MathContext.DECIMAL128);

        analyse.setDepenseMoyenneMensuelle(depenseMoyenneMensuelle);
        analyse.setEpargneMoyenneMensuelle(epargneMoyenneMensuelle);
        analyse.setTauxEpargneMensuel(tauxEpargneMensuel);

        analyse.setResume("Votre résumé financier mensuel montre que...");
        analyse.setRecommandations("Sur la base de votre dépense et épargne moyennes, nous recommandons que...");

        return analyseFinanciereRepository.save(analyse);
    }

    public List<Depense> recupererDepenses(Long employeId) {
        return depenseRepository.findByEmployeEmpId(employeId);
    }

    public List<ObjectifEpargne> recupererObjectifsEpargne(Long employeId) {
        return objectifEpargneRepository.findByEmployeEmpId(employeId);
    }

    public BigDecimal calculerDepenseMoyenneMensuelle(Long employeId) {
        List<Depense> depenses = recupererDepenses(employeId);
        BigDecimal totalDepenses = depenses.stream()
                .map(Depense::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalDepenses.divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP);
    }

    public BigDecimal calculerEpargneMoyenneMensuelle(Long employeId) {
        List<ObjectifEpargne> epargnes = recupererObjectifsEpargne(employeId);
        BigDecimal totalEpargne = epargnes.stream()
                .map(ObjectifEpargne::getMontantActuel)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalEpargne.divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP);
    }

}
