package com.example.pidev.Repositories;

import com.example.pidev.Entities.EvaluationPerformance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationPerformanceRepository extends JpaRepository<EvaluationPerformance,Long> {

    Optional<EvaluationPerformance> findTopByEmployeEmpIdOrderByAnneeDesc(Long employeId);

}
