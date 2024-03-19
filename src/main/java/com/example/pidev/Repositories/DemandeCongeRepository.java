package com.example.pidev.Repositories;

import com.example.pidev.Entities.DemandeConge;
import com.example.pidev.Entities.Departement;
import com.example.pidev.Entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface DemandeCongeRepository extends JpaRepository<DemandeConge, Long> {
    boolean existsByEmployeAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(Employe employe, Date dateDebut, Date dateFin);
    @Query("SELECT count(d) FROM DemandeConge d WHERE d.employe.departement = :departement " +
            "AND d.dateDebut <= :dateFin AND d.dateFin >= :dateDebut")
    long countByDepartementAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
            @Param("departement") Departement departement,
            @Param("dateDebut") Date dateDebut,
            @Param("dateFin") Date dateFin);

    @Query("SELECT dc FROM DemandeConge dc WHERE dc.employe.empId = :employeId")
    List<DemandeConge> findByEmployeId(@Param("employeId") Long employeId);

}
