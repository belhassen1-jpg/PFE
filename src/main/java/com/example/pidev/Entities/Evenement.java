package com.example.pidev.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @ManyToMany
    @JoinTable(
            name = "evenement_participants",
            joinColumns = @JoinColumn(name = "evenement_id"),
            inverseJoinColumns = @JoinColumn(name = "employe_id")
    )
    @JsonIgnoreProperties("evenementsParticipated")
    private Set<Employe> participants;

    @ManyToOne
    @JoinColumn(name = "partenaire_id")
    @JsonIgnore
    private Partenaire partenaire;
}
