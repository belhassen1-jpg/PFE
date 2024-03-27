package com.example.pidev.Repositories;

import com.example.pidev.Entities.DeclarationFiscale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeclarationFiscaleRepository extends JpaRepository<DeclarationFiscale,Long> {
    @Query("SELECT d FROM DeclarationFiscale d WHERE d.bulletinPaie.employe.empId = :empId")
    List<DeclarationFiscale> findByEmployeEmpId(@Param("empId") Long empId);

}
