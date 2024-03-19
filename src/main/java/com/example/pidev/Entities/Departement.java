package com.example.pidev.Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Departement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;
    @OneToOne
    @JoinColumn(name = "chef_departement_id")
    private ChefDepartement chefDepartement;
    @OneToMany(mappedBy = "departement")
    @JsonIgnore
    private Set<Employe> employes;
}
