package com.example.pidev.Entities;

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
public class Rapport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employe_id", referencedColumnName = "emp_id")
    private Employe employe;

    private Long totalHeuresTravaillees;
    private Long totalJoursConges;
    private Integer nombreFormationsParticipees;
    private Integer nombreEvenementsParticipes;
    private String evaluationPerformanceRecente;
    private BigDecimal dernierSalaireBrut;
    private BigDecimal dernierSalaireNet;

    @Temporal(TemporalType.DATE)
    private Date dateRapport;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dernier_bulletin_paie_id")
    private BulletinPaie dernierBulletinPaie;
}
