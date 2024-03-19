package com.example.pidev.Entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EvaluationPerformance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int annee;
    private String commentaire;

    @Enumerated(EnumType.STRING)
    private NiveauPerformance niveauPerformance;
    @ManyToOne
    @JoinColumn(name = "employe_id")
    private Employe employe;
}
