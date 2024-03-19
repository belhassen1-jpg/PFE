package com.example.pidev.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class FeuilleTemps implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date jour;

    @Temporal(TemporalType.TIME)
    private Date heureDebut;

    @Temporal(TemporalType.TIME)
    private Date heureFin;

    private boolean estApprouve;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employe_id", nullable = false)
    @JsonBackReference
    private Employe employe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planning_id", nullable = false)
    @JsonBackReference
    private Planning planning;
}
