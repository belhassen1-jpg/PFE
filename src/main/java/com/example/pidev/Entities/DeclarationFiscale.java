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
public class DeclarationFiscale implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date dateDeclaration;

    private BigDecimal totalRevenuImposable; // Le revenu imposable pour la période de déclaration
    private BigDecimal montantImpotDu; // Le montant total de l'impôt dû
    private BigDecimal montantCotisationsSocialesDu; // Le montant total des cotisations sociales dues
    private String referenceDeclaration; // Une référence unique pour la déclaration
    private String autoriteFiscale; // L'autorité fiscale responsable


    @OneToOne
    @JoinColumn(name = "bulletinPaie_id", referencedColumnName = "id")
    private BulletinPaie bulletinPaie;

}
