package com.example.pidev.Repositories;

import com.example.pidev.Entities.ObjectifEpargne;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObjectifEpargneRepository extends JpaRepository<ObjectifEpargne,Long> {
    List<ObjectifEpargne> findByEmployeEmpId(Long empId);
}
