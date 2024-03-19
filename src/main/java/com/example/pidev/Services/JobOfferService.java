package com.example.pidev.Services;

import com.example.pidev.Entities.JobOffer;
import com.example.pidev.Entities.User;
import com.example.pidev.Repositories.JobOfferRepository;
import com.example.pidev.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;



@Service
@AllArgsConstructor
@NoArgsConstructor
public class JobOfferService {
    @Autowired
    private JobOfferRepository jobOfferRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailService;

    public JobOffer addJobOffer(JobOffer jobOffer) {
        return jobOfferRepository.save(jobOffer);
    }
    public void notifyUsersAboutNewJobOffer(JobOffer jobOffer) {
        // Trouver des utilisateurs intéressés par cette catégorie d'emploi
        List<User> interestedUsers = userRepository.findUsersByPreferences(jobOffer.getCategory(), jobOffer.getLocation());

        for (User user : interestedUsers) {
            try {
                sendNotificationEmail(user, jobOffer);
            } catch (MessagingException e) {
                e.printStackTrace();
                // Gérer l'exception ici (log, etc.)
            }
        }
    }
    private void sendNotificationEmail(User user, JobOffer jobOffer) throws MessagingException {
        String subject = "Nouvelle offre d'emploi disponible !";
        String message = String.format("Bonjour %s %s,\n\nUne nouvelle offre d'emploi '%s' dans '%s' est disponible.\nDescription: %s\n\nCordialement, Votre équipe",
                user.getFirstName(), user.getLastName(), jobOffer.getTitle(), jobOffer.getLocation(), jobOffer.getDescription());

        mailService.sendEmail(user.getEmail(), message);
    }


    public JobOffer updateJobOffer(Long id, JobOffer jobOfferDetails) {
        JobOffer jobOffer = jobOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job Offer not found for this id :: " + id));
        jobOffer.setTitle(jobOfferDetails.getTitle());
        jobOffer.setDescription(jobOfferDetails.getDescription());
        jobOffer.setSalary(jobOfferDetails.getSalary());
        jobOffer.setRequiredExperienceYears(jobOfferDetails.getRequiredExperienceYears());
        jobOffer.setLocation(jobOfferDetails.getLocation());
        jobOffer.setCategory(jobOfferDetails.getCategory());
        jobOffer.setKeywords(new HashSet<>(jobOfferDetails.getKeywords()));
        return jobOfferRepository.save(jobOffer);
    }

    public void deleteJobOffer(Long id) {
        JobOffer jobOffer = jobOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job Offer not found for this id :: " + id));
        jobOfferRepository.delete(jobOffer);
    }

    public List<JobOffer> getAllJobOffers() {
        return jobOfferRepository.findAll();
    }

    public JobOffer getJobOfferById(Long id) {
        Optional<JobOffer> jobOffer = jobOfferRepository.findById(id);
        if(jobOffer.isPresent()) {
            return jobOffer.get();
        } else {
            throw new RuntimeException("Job Offer not found for this id :: " + id);
        }
    }

    public List<JobOffer> findJobOffersWithFilters(String category, String location) {
        if (category != null && !category.isEmpty() && location != null && !location.isEmpty()) {
            return jobOfferRepository.findByCategoryAndLocation(category, location);
        } else if (category != null && !category.isEmpty()) {
            return jobOfferRepository.findByCategory(category);
        } else if (location != null && !location.isEmpty()) {
            return jobOfferRepository.findByLocation(location);
        } else {
            return jobOfferRepository.findAll();
        }
    }



}
