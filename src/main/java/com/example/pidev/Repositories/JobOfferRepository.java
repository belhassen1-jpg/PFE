package com.example.pidev.Repositories;

import com.example.pidev.Entities.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long>{
    // Trouver les offres d'emploi par catégorie et localisation
    List<JobOffer> findByCategoryAndLocation(String category, String location);

    // Trouver les offres d'emploi par catégorie seulement
    List<JobOffer> findByCategory(String category);

    // Trouver les offres d'emploi par localisation seulement
    List<JobOffer> findByLocation(String location);

    // Trouver toutes les offres d'emploi (aucun filtre)
    List<JobOffer> findAll();

}
