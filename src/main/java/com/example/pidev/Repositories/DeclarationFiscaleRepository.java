package com.example.pidev.Repositories;

import com.example.pidev.Entities.DeclarationFiscale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeclarationFiscaleRepository extends JpaRepository<DeclarationFiscale,Long> {
    List<DeclarationFiscale> findByEmployeEmpId(Long empId);

}
