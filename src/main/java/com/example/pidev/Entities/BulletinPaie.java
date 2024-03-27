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
@PrimaryKeyJoinColumn(name = "id")
public class BulletinPaie extends Paie {

    @Temporal(TemporalType.DATE)
    private Date dateEmission; // La date d'émission du bulletin de paie

    @Temporal(TemporalType.DATE)
    private Date periodeDebut; // Le début de la période de paie

    @Temporal(TemporalType.DATE)
    private Date periodeFin; // La fin de la période de paie

    private String commentaire; // Un champ pour des commentaires ou des notes spéciales

    private String nomEntreprise;
    private String adresseEntreprise;


    @OneToOne
    @JoinColumn(name = "rapport_id", referencedColumnName = "id")
    private Rapport rapport;
    @OneToOne
    @JoinColumn(name = "declarationFiscale_id", referencedColumnName = "id")
    private DeclarationFiscale declarationFiscale;


}
