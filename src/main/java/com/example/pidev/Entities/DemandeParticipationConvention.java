package com.example.pidev.Entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DemandeParticipationConvention implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Employe employe;

    @ManyToOne
    private Convention convention;

    private boolean estValidee; // False par défaut, true si la demande est validée par le responsable
}