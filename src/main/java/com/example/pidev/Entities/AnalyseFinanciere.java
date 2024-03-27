package com.example.pidev.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class AnalyseFinanciere implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resume;
    private String recommandations;
    @Temporal(TemporalType.DATE)
    private Date dateAnalyse;

    @ManyToOne
    @JoinColumn(name = "employe_id")
    private Employe employe;

    private BigDecimal tauxEpargneMensuel; // Calculé comme une portion du revenu
    private BigDecimal depenseMoyenneMensuelle;
    private BigDecimal epargneMoyenneMensuelle;
}
