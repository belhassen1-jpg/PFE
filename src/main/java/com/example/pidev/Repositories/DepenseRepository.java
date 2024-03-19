package com.example.pidev.Repositories;

import com.example.pidev.Entities.Depense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepenseRepository extends JpaRepository<Depense,Long> {
    List<Depense> findByEmployeEmpId(Long empId);
}
