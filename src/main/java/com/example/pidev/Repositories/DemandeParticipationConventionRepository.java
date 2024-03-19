package com.example.pidev.Repositories;

import com.example.pidev.Entities.DemandeParticipationConvention;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemandeParticipationConventionRepository extends JpaRepository<DemandeParticipationConvention, Long> {
    List<DemandeParticipationConvention> findByConventionIdAndEstValidee(Long conventionId, boolean estValidee);

}
