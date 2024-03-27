package com.example.pidev.Repositories;

import com.example.pidev.Entities.DemandeParticipationConvention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DemandeParticipationConventionRepository extends JpaRepository<DemandeParticipationConvention, Long> {
    List<DemandeParticipationConvention> findByConventionIdAndEstValidee(Long conventionId, boolean estValidee);
    @Query("SELECT dpc FROM DemandeParticipationConvention dpc WHERE dpc.employe.empId = :employeId")
    List<DemandeParticipationConvention> findByEmployeId(@Param("employeId") Long employeId);
    @Query("SELECT dpc FROM DemandeParticipationConvention dpc WHERE dpc.employe.empId = :empId AND dpc.convention.id = :conventionId")
    List<DemandeParticipationConvention> findByEmployeIdAndConventionId(@Param("empId") Long empId, @Param("conventionId") Long conventionId);


}
