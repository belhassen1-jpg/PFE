package com.example.pidev.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Paie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date datePaie;

    private BigDecimal tauxHoraire; // Taux horaire standard
    private BigDecimal heuresTravaillees; // Heures travaillées dans le cycle de paie
    private BigDecimal heuresSupplementaires; // Heures supplémentaires effectuées
    private BigDecimal tauxHeuresSupplementaires; // Taux pour les heures supplémentaires
    private BigDecimal montantPrimes; // Total des primes
    private BigDecimal montantDeductions; // Total des déductions (ex: avance sur salaire, matériel, etc.)

    private BigDecimal cotisationsSociales;
    private BigDecimal impotSurRevenu;
    private BigDecimal salaireNet;
    private BigDecimal SalaireBrut;



    @ManyToOne
    @JoinColumn(name = "employe_id")
    @JsonBackReference
    private Employe employe;
}
