package com.example.pidev.Repositories;
import com.example.pidev.Entities.Rapport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RapportRepository extends JpaRepository<Rapport, Long> {
    Optional<Rapport> findByEmployeEmpId(Long empId);
}
