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
public class Depense implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categorie;
    private BigDecimal montant;
    private String description;
    @Temporal(TemporalType.DATE)
    private Date dateDepense;

    @ManyToOne
    @JoinColumn(name = "employe_id")
    private Employe employe;
}
