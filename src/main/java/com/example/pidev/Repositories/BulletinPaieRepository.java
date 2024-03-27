package com.example.pidev.Repositories;

import com.example.pidev.Entities.BulletinPaie;
import com.example.pidev.Entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BulletinPaieRepository extends JpaRepository<BulletinPaie,Long> {
    @Query("SELECT bp FROM BulletinPaie bp WHERE bp.employe.empId = :employeId ORDER BY bp.dateEmission DESC")
    Optional<BulletinPaie> findTopByEmployeIdOrderByDateEmissionDesc(@Param("employeId") Long empId);
    List<BulletinPaie> findByEmployeEmpId(Long empId);
}
