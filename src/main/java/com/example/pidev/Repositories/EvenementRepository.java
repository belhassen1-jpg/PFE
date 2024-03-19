package com.example.pidev.Repositories;

import com.example.pidev.Entities.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    List<Evenement> findByPartenaireId(Long partenaireId);


    @Query("SELECT e FROM Evenement e JOIN FETCH e.participants")
    List<Evenement> findAllWithParticipants();
    @Query("SELECT e FROM Evenement e JOIN e.participants p WHERE p.empId = :empId")
    List<Evenement> findByParticipantsId(@Param("empId") Long empId);
}
