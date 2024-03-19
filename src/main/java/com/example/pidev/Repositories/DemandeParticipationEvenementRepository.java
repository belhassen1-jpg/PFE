package com.example.pidev.Repositories;

import com.example.pidev.Entities.DemandeParticipationEvenement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemandeParticipationEvenementRepository extends JpaRepository<DemandeParticipationEvenement, Long> {
    List<DemandeParticipationEvenement> findByEvenementIdAndEstValidee(Long evenementId, boolean estValidee);

}
