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
public class ObjectifEpargne implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal objectifMontant;
    private BigDecimal montantActuel;
    private String description;
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Temporal(TemporalType.DATE)
    private Date dateFin;

    @ManyToOne
    @JoinColumn(name = "employe_id")
    @JsonBackReference

    private Employe employe;

    // Progression en pourcentage pour faciliter l'affichage et la compréhension de l'avancement
    @Transient
    private BigDecimal progression;
}
