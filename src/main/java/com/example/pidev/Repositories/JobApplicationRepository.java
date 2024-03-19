package com.example.pidev.Repositories;

import com.example.pidev.Entities.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    @Query("SELECT ja FROM JobApplication ja WHERE ja.applicant.idUser = :idUser")
    List<JobApplication> findByApplicantId(@Param("idUser") Long idUser);
    List<JobApplication> findByJobOfferId(Long jobOfferId);

    @Query("SELECT ja FROM JobApplication ja WHERE ja.applicant.idUser = :idUser AND ja.jobOffer.id = :jobOfferId")
    Optional<JobApplication> findByApplicantIdAndJobOfferId(@Param("idUser") Long idUser, @Param("jobOfferId") Long jobOfferId);


}
