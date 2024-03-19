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
public class BulletinPaie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal salaireBrut;
    private BigDecimal salaireNet;

    @Temporal(TemporalType.DATE)
    private Date dateEmission; // La date d'émission du bulletin de paie

    @Temporal(TemporalType.DATE)
    private Date periodeDebut; // Le début de la période de paie

    @Temporal(TemporalType.DATE)
    private Date periodeFin; // La fin de la période de paie

    // Informations supplémentaires spécifiques au bulletin
    private String commentaire; // Un champ pour des commentaires ou des notes spéciales

    // Informations de l'entreprise (si celles-ci ne sont pas stockées ailleurs dans une entité distincte)
    private String nomEntreprise;
    private String adresseEntreprise;

    // L'association avec l'entité Paie
    @OneToOne
    @JoinColumn(name = "paie_id", referencedColumnName = "id")
    @JsonBackReference
    private Paie detailsPaie;

    @ManyToOne
    @JoinColumn(name = "employe_id") // This column should exist in your BulletinPaie table.
    @JsonBackReference
    private Employe employe;

}
