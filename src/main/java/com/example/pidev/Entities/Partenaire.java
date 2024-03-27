package com.example.pidev.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Partenaire implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String secteur;
    private String adresse;

    // Relation inverse pour accéder aux conventions liées
    @OneToMany(mappedBy = "partenaire")
    private Set<Convention> conventions;
    @OneToMany(mappedBy = "partenaire")
    private Set<Evenement> evenements;
}
