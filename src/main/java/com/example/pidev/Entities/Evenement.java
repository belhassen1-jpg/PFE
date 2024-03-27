package com.example.pidev.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Evenement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateHeure;

    private String lieu;

    @OneToMany(mappedBy = "evenement")
    private Set<DemandeParticipationEvenement> demandeParticipationevenments;

    @ManyToOne
    @JoinColumn(name = "partenaire_id")
    private Partenaire partenaire;
}
