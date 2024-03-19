package com.example.pidev.Repositories;

import com.example.pidev.Entities.Departement;
import com.example.pidev.Entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeRepository extends JpaRepository<Employe, Long> {
    long countByDepartement(Departement departement);
}
