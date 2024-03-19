package com.example.pidev.Entities;

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
public class DemandeDémission implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String motif;

    @Enumerated(EnumType.STRING)
    private StatutDemande statut;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDemande;

    @OneToOne
    @JoinColumn(name = "employe_id")
    private Employe employe;

}
