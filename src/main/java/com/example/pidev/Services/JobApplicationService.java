package com.example.pidev.Services;

import com.example.pidev.DTO.JobApplicationDto;
import com.example.pidev.Entities.JobApplication;
import com.example.pidev.Entities.JobOffer;
import com.example.pidev.Entities.StatutDemande;
import com.example.pidev.Entities.User;
import com.example.pidev.Repositories.JobApplicationRepository;
import com.example.pidev.Repositories.JobOfferRepository;
import com.example.pidev.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationService {
    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Autowired
    private JavaMailSender mailSender; // Supposons que vous avez configuré JavaMailSender

    // Ajoutez l'adresse email du RH
    private static final String RH_EMAIL = "belhassen.knani@esprit.tn";


//Pour permettre aux utilisateurs de suivre l'état de leur candidature,
// vous pouvez fournir une méthode dans votre service qui récupère les candidatures d'un utilisateur donné
    public List<JobApplication> getApplicationsByUserId(Long userId) {
        return jobApplicationRepository.findByApplicantId(userId);
    }
//Pour récupérer toutes les candidatures pour une offre d'emploi spécifique,
// vous pouvez ajouter une méthode dans votre service de candidature.

    public List<JobApplication> getApplicationsForJobOffer(Long jobOfferId) {
        return jobApplicationRepository.findByJobOfferId(jobOfferId);
    }

    // pour l'utilisateur que veut consulter sa condidature :

    public JobApplicationDto getApplicationDetails(Long userId, Long jobOfferId) {
        JobApplication application = jobApplicationRepository.findByApplicantIdAndJobOfferId(userId, jobOfferId)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée."));

        JobApplicationDto applicationDto = new JobApplicationDto();
        BeanUtils.copyProperties(application, applicationDto);
        applicationDto.setJobOfferId(application.getJobOffer().getId());
        applicationDto.setUserId(application.getApplicant().getIdUser()); // Assurez-vous que votre entité JobApplication a un lien vers l'utilisateur
        applicationDto.setStatus(application.getStatus()); // Copier le statut de la candidature

        return applicationDto;
    }

    @Transactional
    public JobApplicationDto submitApplication(JobApplicationDto applicationDto, String resumePath, String coverLetterPath) {
        JobApplication application = new JobApplication();

        // Récupérer l'offre d'emploi et l'utilisateur par leurs IDs
        JobOffer jobOffer = jobOfferRepository.findById(applicationDto.getJobOfferId())
                .orElseThrow(() -> new RuntimeException("Offre d'emploi non trouvée."));
        User applicant = userRepository.findById(applicationDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé."));

        // Copier les propriétés du DTO vers l'entité JobApplication
        BeanUtils.copyProperties(applicationDto, application);

        // Définir les chemins des fichiers pour le CV et la lettre de motivation
        application.setResumePath(resumePath);
        application.setCoverLetterPath(coverLetterPath);

        // Assigner l'offre d'emploi et l'utilisateur
        application.setJobOffer(jobOffer);
        application.setApplicant(applicant);

        // Initialiser le statut à EN_ATTENTE
        application.setStatus(StatutDemande.EN_ATTENTE);

        // Sauvegarder l'entité JobApplication dans la base de données
        JobApplication savedApplication = jobApplicationRepository.save(application);

        // Envoyer un email au RH pour notifier de la nouvelle candidature
        sendEmailToRH(savedApplication);

        // Convertir l'entité sauvegardée en DTO pour le retour
        JobApplicationDto savedDto = new JobApplicationDto();
        BeanUtils.copyProperties(savedApplication, savedDto);
        savedDto.setJobOfferId(savedApplication.getJobOffer().getId());
        savedDto.setUserId(savedApplication.getApplicant().getIdUser());

        return savedDto;
    }

    private void sendEmailToRH(JobApplication application) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@example.com");
        message.setTo(RH_EMAIL);
        message.setSubject("Nouvelle candidature soumise");
        message.setText(createEmailContent(application));
        mailSender.send(message);
    }

    private String createEmailContent(JobApplication application) {
        return String.format(
                "Une nouvelle candidature a été soumise pour l'offre d'emploi: %s\n" +
                        "Nom du candidat: %s\n" +
                        "Email: %s\n" +
                        "Téléphone: %s\n" +
                        "Adresse: %s\n" +
                        "Années d'expérience: %d\n" +
                        "CV: %s\n" +
                        "Lettre de motivation: %s\n",
                application.getJobOffer().getTitle(),
                application.getApplicantName(),
                application.getApplicantEmail(),
                application.getApplicantPhone(),
                application.getApplicantAddress(),
                application.getYearsOfExperience(),
                application.getResumePath(),
                application.getCoverLetterPath()
        );
    }


    @Transactional
    public void updateApplicationStatus(Long applicationId, StatutDemande newStatus) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found."));

        application.setStatus(newStatus);
        jobApplicationRepository.save(application);

        sendStatusUpdateEmail(application);
    }

    private void sendStatusUpdateEmail(JobApplication application) {
        String subject;
        String content;

        if (application.getStatus() == StatutDemande.ACCEPTEE) {
            subject = "Your application has been accepted!";
            content = "Congratulations! Your application for the position of " + application.getJobOffer().getTitle() + " has been accepted.";
        } else if (application.getStatus() == StatutDemande.REFUSEE) {
            subject = "Your application has been rejected";
            content = "We regret to inform you that your application for the position of " + application.getJobOffer().getTitle() + " has been rejected.";
        } else {
            // Handle other statuses if needed
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(application.getApplicantEmail());
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }



}
