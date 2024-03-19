package com.example.pidev.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@PrimaryKeyJoinColumn(name = "emp_id")
public class ChefDepartement extends Employe {
    private String specialisation;
    @OneToOne(mappedBy = "chefDepartement")
    @JsonIgnore
    private Departement departementDirige;
}
