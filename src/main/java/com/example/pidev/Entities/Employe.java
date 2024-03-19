package com.example.pidev.Entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Inheritance(strategy = InheritanceType.JOINED) // Utiliser la stratégie de jointure

public class Employe implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id", unique = true, nullable = false)
    private Long empId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

    @Column
    private String title;

    private BigDecimal tauxHoraire;
    private BigDecimal tauxHeuresSupplementaires;
    private BigDecimal montantPrimes;
    private BigDecimal montantDeductions;

    @ManyToOne
    private Departement departement;

    @OneToMany(mappedBy = "employe")
    @JsonManagedReference
    private Set<Paie> Paiess;

    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    @JsonManagedReference // pour sérialiser normalement les feuilles de temps dans l'employé
    private List<FeuilleTemps> feuillesDeTemps;


    @ManyToMany(mappedBy = "participants")
    @JsonBackReference
    private Set<SessionFormation> sessionsFormation;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iduser")
    @JsonIgnore
    private User user;

    @OneToOne(mappedBy = "employe")
    private DemandeDémission demandeDémission;

    @OneToMany(mappedBy = "employe")
    private Set<DemandeConge> demandesConge;

    @OneToMany(mappedBy = "employe")
    private List<EvaluationPerformance> evaluationsPerformance;

    @ManyToMany(mappedBy = "participants")
    @JsonBackReference
    private Set<Convention> conventionsParticipated;
    @ManyToMany(mappedBy = "participants")
    @JsonBackReference
    private Set<Evenement> evenementsParticipated;

    @OneToOne(mappedBy = "employe", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private Rapport rapport;

    @OneToMany(mappedBy = "employe")
    @JsonManagedReference
    private List<DeclarationFiscale> declarationsFiscales;

    // Relation avec les dépenses
    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Depense> depenses = new HashSet<>();

    // Relation avec les objectifs d'épargne
    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<ObjectifEpargne> objectifsEpargne = new HashSet<>();

    // Relation avec les analyses financières
    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<AnalyseFinanciere> analysesFinancieres = new HashSet<>();
}
