package com.example.pidev.Repositories;

import com.example.pidev.Entities.Paie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaieRepository extends JpaRepository<Paie,Long> {

    Optional<Paie> findTopByEmployeEmpIdOrderByDatePaieDesc(Long empId);


}
