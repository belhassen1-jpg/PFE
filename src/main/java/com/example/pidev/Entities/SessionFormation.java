package com.example.pidev.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SessionFormation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String intitule;
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateHeure;

    private String lieu;

    @ManyToMany
    @JoinTable(
            name = "inscription_formation",
            joinColumns = @JoinColumn(name = "session_formation_id"),
            inverseJoinColumns = @JoinColumn(name = "employe_id")
    )
    private Set<Employe> participants;
}
