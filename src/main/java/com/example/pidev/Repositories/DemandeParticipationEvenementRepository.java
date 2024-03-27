package com.example.pidev.Repositories;

import com.example.pidev.Entities.DemandeParticipationEvenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DemandeParticipationEvenementRepository extends JpaRepository<DemandeParticipationEvenement, Long> {
    @Query("SELECT dr from DemandeParticipationEvenement dr WHERE dr.employe.empId = :employeId AND dr.estValidee = :estValidee")
    List<DemandeParticipationEvenement> findByEvenementIdAndEstValidee(@Param("employeId") Long employeId, @Param("estValidee") boolean estValidee);
    @Query("SELECT d from DemandeParticipationEvenement d WHERE d.employe.empId = :employeId AND d.estValidee = :estValidee")
    List<DemandeParticipationEvenement> findByEmployeIdAndEstValidee(@Param("employeId") Long employeId, @Param("estValidee") boolean estValidee);
    @Query("SELECT dpe FROM DemandeParticipationEvenement dpe WHERE dpe.employe.empId = :employeId AND dpe.evenement.id = :evenementId AND dpe.estValidee = :estValidee")
    List<DemandeParticipationEvenement> findByEmployeIdAndEvenementIdAndEstValidee(@Param("employeId") Long employeId, @Param("evenementId") Long evenementId, @Param("estValidee") boolean estValidee);
}
