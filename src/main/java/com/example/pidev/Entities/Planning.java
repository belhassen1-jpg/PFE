package com.example.pidev.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Planning implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Autres attributs pour définir le planning, comme la période de validité
    @Temporal(TemporalType.DATE)
    private Date dateDebutValidite;

    @Temporal(TemporalType.DATE)
    private Date dateFinValidite;

    @OneToMany(mappedBy = "planning", fetch = FetchType.LAZY)
    private List<FeuilleTemps> feuillesDeTemps;
}
