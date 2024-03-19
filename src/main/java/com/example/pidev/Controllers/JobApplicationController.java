package com.example.pidev.Controllers;

import com.example.pidev.DTO.JobApplicationDto;
import com.example.pidev.Entities.JobApplication;
import com.example.pidev.Entities.StatutDemande;
import com.example.pidev.Services.FileStorageService;
import com.example.pidev.Services.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/applications")
public class JobApplicationController {
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private JobApplicationService jobApplicationService;

    @PostMapping("/submit")
    public ResponseEntity<JobApplicationDto> submitApplication(
            @RequestParam("jobOfferId") Long jobOfferId,
            @RequestParam("userId") Long userId,
            @RequestParam("resume") MultipartFile resume,
            @RequestParam("coverLetter") MultipartFile coverLetter,
            JobApplicationDto applicationDto) {
        try {
            // Stocker les fichiers et récupérer les chemins
            String resumePath = fileStorageService.storeFile(resume);
            String coverLetterPath = fileStorageService.storeFile(coverLetter);

            JobApplicationDto savedApplicationDto = jobApplicationService.submitApplication(applicationDto, resumePath, coverLetterPath);
            return new ResponseEntity<>(savedApplicationDto, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/updateStatus/{applicationId}")
    public ResponseEntity<String> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam StatutDemande status) {
        jobApplicationService.updateApplicationStatus(applicationId, status);
        return ResponseEntity.ok("The application status has been updated successfully.");
    }

    // Récupérer les candidatures par ID utilisateur
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<JobApplication>> getApplicationsByUserId(@PathVariable Long userId) {
        List<JobApplication> applications = jobApplicationService.getApplicationsByUserId(userId);
        return ResponseEntity.ok(applications);
    }

    // Récupérer les candidatures pour une offre d'emploi spécifique
    @GetMapping("/job-offer/{jobOfferId}")
    public ResponseEntity<List<JobApplication>> getApplicationsForJobOffer(@PathVariable Long jobOfferId) {
        List<JobApplication> applications = jobApplicationService.getApplicationsForJobOffer(jobOfferId);
        return ResponseEntity.ok(applications);
    }

    // Obtenir les détails d'une candidature spécifique
    @GetMapping("/{userId}/{jobOfferId}")
    public ResponseEntity<JobApplicationDto> getApplicationDetails(@PathVariable Long userId, @PathVariable Long jobOfferId) {
        JobApplicationDto applicationDetails = jobApplicationService.getApplicationDetails(userId, jobOfferId);
        return ResponseEntity.ok(applicationDetails);
    }


}
