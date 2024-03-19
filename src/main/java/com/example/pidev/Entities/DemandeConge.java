package com.example.pidev.Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DemandeConge implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    private Date dateFin;

    @Enumerated(EnumType.STRING)
    private TypeConge type;

    @Enumerated(EnumType.STRING)
    private StatutDemande statut;

    @ManyToOne
    private Employe employe;


}
