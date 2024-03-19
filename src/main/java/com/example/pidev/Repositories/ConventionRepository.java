package com.example.pidev.Repositories;

import com.example.pidev.Entities.Convention;
import com.example.pidev.Entities.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ConventionRepository extends JpaRepository<Convention, Long> {
    List<Convention> findByPartenaireId(Long partenaireId);

    @Query("SELECT c FROM Convention c JOIN FETCH c.participants")
    List<Convention> findAllWithParticipants();
    @Query("SELECT c FROM Convention c JOIN c.participants p WHERE p.empId = :empId")
    List<Convention> findByParticipantsId(@Param("empId") Long empId);
}
