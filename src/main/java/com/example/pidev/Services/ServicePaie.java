package com.example.pidev.Services;

import com.example.pidev.Entities.*;
import com.example.pidev.Repositories.BulletinPaieRepository;
import com.example.pidev.Repositories.DeclarationFiscaleRepository;
import com.example.pidev.Repositories.EmployeRepository;
import com.example.pidev.Repositories.PaieRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ServicePaie {
    @Autowired
    private EmployeRepository employeRepository;
    @Autowired
    private PaieRepository paieRepository;
    @Autowired
    private BulletinPaieRepository bulletinPaieRepository;
    @Autowired
    private DeclarationFiscaleRepository declarationFiscaleRepository;
    @Autowired
    private PlanningService planningService;




    // Méthodes pour interagir avec l'entité Paie

    @Transactional
    public Paie calculerPaie(Long employeId) {

        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'id : " + employeId));

        // Supposons que vous ayez une méthode pour obtenir les feuilles de temps de l'employé pour la période en cours.
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date dateDebut = cal.getTime();

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date dateFin = cal.getTime();
        List<FeuilleTemps> feuillesDeTemps = planningService.obtenirFeuillesDeTempsPourPeriode(employeId, dateDebut, dateFin);

        // Initialisation des totaux
        BigDecimal totalHeuresTravaillees = BigDecimal.ZERO;
        BigDecimal totalHeuresSup = BigDecimal.ZERO;
        BigDecimal seuilHeuresNormales = BigDecimal.valueOf(8);
        // Parcourir les feuilles de temps pour calculer les totaux
        for (FeuilleTemps feuille : feuillesDeTemps) {
            long diff = feuille.getHeureFin().getTime() - feuille.getHeureDebut().getTime();
            BigDecimal heuresTravaillees = BigDecimal.valueOf(diff / (60 * 60 * 1000) % 24);
            totalHeuresTravaillees = totalHeuresTravaillees.add(heuresTravaillees);

            // Calcul des heures supplémentaires pour chaque feuille
            if (heuresTravaillees.compareTo(seuilHeuresNormales) > 0) {
                BigDecimal heuresSup = heuresTravaillees.subtract(seuilHeuresNormales);
                totalHeuresSup = totalHeuresSup.add(heuresSup);
            }
        }

        Paie paie = new Paie();
        paie.setEmploye(employe);
        paie.setDatePaie(new Date());
        paie.setHeuresTravaillees(totalHeuresTravaillees);
        paie.setHeuresSupplementaires(totalHeuresSup);
        paie.setTauxHoraire(employe.getTauxHoraire());
        paie.setTauxHeuresSupplementaires(employe.getTauxHeuresSupplementaires());
        paie.setMontantPrimes(employe.getMontantPrimes());
        paie.setMontantDeductions(employe.getMontantDeductions());

            // Calcul du salaire brut
            BigDecimal salaireBrut = employe.getTauxHoraire().multiply(totalHeuresTravaillees);
            salaireBrut = salaireBrut.add(employe.getTauxHeuresSupplementaires().multiply(totalHeuresSup));

            // Ajouter les primes et déductions
            BigDecimal montantPrimes = employe.getMontantPrimes();
            BigDecimal montantDeductions = employe.getMontantDeductions();
            salaireBrut = salaireBrut.add(montantPrimes).subtract(montantDeductions);



            // Calcul des cotisations et impôts
            BigDecimal tauxCotisationsSociales = new BigDecimal("0.0918"); // 9,18% total, à ajuster selon les taux actuels
            BigDecimal cotisationsSociales = salaireBrut.multiply(tauxCotisationsSociales);
            paie.setCotisationsSociales(cotisationsSociales);

            BigDecimal impotSurRevenu = BigDecimal.ZERO;

            if (salaireBrut.compareTo(new BigDecimal("200")) <= 0) {
                impotSurRevenu = BigDecimal.ZERO;
            } else if (salaireBrut.compareTo(new BigDecimal("200")) > 0 && salaireBrut.compareTo(new BigDecimal("800")) <= 0) {
                impotSurRevenu = (salaireBrut.subtract(new BigDecimal("200"))).multiply(new BigDecimal("0.26"));
            } else {
                impotSurRevenu = (salaireBrut.subtract(new BigDecimal("800"))).multiply(new BigDecimal("0.28"))
                        .add(new BigDecimal("800").subtract(new BigDecimal("200")).multiply(new BigDecimal("0.26")));
            }

            paie.setImpotSurRevenu(impotSurRevenu);

            // Calcul du salaire net
            BigDecimal salaireNet = salaireBrut.subtract(cotisationsSociales).subtract(impotSurRevenu);
            paie.setSalaireNet(salaireNet);
            paie.setSalaireBrut(salaireBrut);

        return paieRepository.save(paie);
        }





    @Transactional
    public BulletinPaie genererBulletinPaie(Long employeId) {
        Paie paie = paieRepository.findTopByEmployeEmpIdOrderByDatePaieDesc(employeId)
                .orElseThrow(() -> new RuntimeException("Dernière paie non trouvée pour l'employé avec l'id : " + employeId));

            BulletinPaie bulletin = new BulletinPaie();
            // Affecter les valeurs de Paie à BulletinPaie
             bulletin.setDetailsPaie(paie);
             bulletin.setEmploye(paie.getEmploye());
            bulletin.setSalaireBrut(paie.getSalaireBrut());
            bulletin.setSalaireNet(paie.getSalaireNet());

            // La date d'émission est aujourd'hui
            bulletin.setDateEmission(new Date());

            // Supposons que la période de début et de fin soit basée sur le mois de la datePaie de l'objet Paie
            Calendar cal = Calendar.getInstance();
            cal.setTime(paie.getDatePaie());
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            Date periodeDebut = cal.getTime();

            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date periodeFin = cal.getTime();

            bulletin.setPeriodeDebut(periodeDebut);
            bulletin.setPeriodeFin(periodeFin);

            // Informations supplémentaires et commentaire
            bulletin.setCommentaire("Bulletin généré automatiquement pour la période indiquée.");
            // L'ajout des informations d'entreprise dépend de votre modèle de données
            // Ici, elles sont ajoutées statiquement, mais elles pourraient être récupérées à partir de l'employé ou de la configuration de l'application
            bulletin.setNomEntreprise("Nom de l'Entreprise");
            bulletin.setAdresseEntreprise("Adresse de l'Entreprise");
        return bulletinPaieRepository.save(bulletin);
    }





    public DeclarationFiscale genererDeclarationFiscale(Employe employe) {
        if (employe == null) {
            throw new IllegalArgumentException("L'objet Employe ne peut pas être null.");
        }

        // Récupérer la dernière paie pour cet employé
        Paie dernierePaie = paieRepository.findTopByEmployeEmpIdOrderByDatePaieDesc(employe.getEmpId())
                .orElseThrow(() -> new RuntimeException("Dernière paie non trouvée pour l'employé : " + employe.getEmpId()));

        // Créer une nouvelle instance de DeclarationFiscale
        DeclarationFiscale declaration = new DeclarationFiscale();
        declaration.setEmploye(employe);
        declaration.setPaie(dernierePaie);
        declaration.setDateDeclaration(new Date()); // Date actuelle pour la déclaration

        // Utiliser les informations de la dernière paie pour définir les champs de la déclaration fiscale
        declaration.setTotalRevenuImposable(dernierePaie.getSalaireBrut());
        declaration.setMontantImpotDu(dernierePaie.getImpotSurRevenu());
        declaration.setMontantCotisationsSocialesDu(dernierePaie.getCotisationsSociales());

        // Générer une référence unique pour la déclaration fiscale
        String referenceDeclaration = "DF-" + employe.getEmpId()+ "-" + System.currentTimeMillis();
        declaration.setReferenceDeclaration(referenceDeclaration);

        // Nom et adresse de l'autorité fiscale (doivent être fournis ou configurés ailleurs dans l'application)
        declaration.setAutoriteFiscale("Direction Générale des Impôts de Tunisie");

        // Enregistrer la déclaration fiscale dans la base de données
        return declarationFiscaleRepository.save(declaration);
    }




    // Méthode pour obtenir les bulletins de paie d'un employé
    public List<BulletinPaie> obtenirBulletinsPaie(Long employeId) {
        return bulletinPaieRepository.findByEmployeEmpId(employeId);
    }
    public BulletinPaie obtenirDernierBulletinPaie(Long employeId) {
        return bulletinPaieRepository.findTopByEmployeIdOrderByDateEmissionDesc(employeId).orElse(null);
    }


    public Paie obtenirDernierePaie(Long employeId) {
        return paieRepository.findTopByEmployeEmpIdOrderByDatePaieDesc(employeId).orElse(null);
    }


    // Méthode pour obtenir les déclarations fiscales d'un employé
    public List<DeclarationFiscale> obtenirDeclarationsFiscales(Long employeId) {
        return declarationFiscaleRepository.findByEmployeEmpId(employeId);
    }


}


