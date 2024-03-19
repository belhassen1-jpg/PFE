package com.example.pidev.Controllers;

import com.example.pidev.Entities.JobOffer;
import com.example.pidev.Services.JobOfferService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api/joboffers")
public class JobOfferController {
    @Autowired
    private JobOfferService jobOfferService;

    @PostMapping("/add")
    public ResponseEntity<JobOffer> addJobOffer(@RequestBody JobOffer jobOffer) {
        JobOffer addedJobOffer = jobOfferService.addJobOffer(jobOffer);
        jobOfferService.notifyUsersAboutNewJobOffer(addedJobOffer);
        return new ResponseEntity<>(addedJobOffer, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<JobOffer> updateJobOffer(@PathVariable Long id, @RequestBody JobOffer jobOfferDetails) {
        JobOffer updatedJobOffer = jobOfferService.updateJobOffer(id, jobOfferDetails);
        return new ResponseEntity<>(updatedJobOffer, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteJobOffer(@PathVariable Long id) {
        jobOfferService.deleteJobOffer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<JobOffer>> getAllJobOffers() {
        List<JobOffer> jobOffers = jobOfferService.getAllJobOffers();
        return new ResponseEntity<>(jobOffers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobOffer> getJobOfferById(@PathVariable Long id) {
        JobOffer jobOffer = jobOfferService.getJobOfferById(id);
        return new ResponseEntity<>(jobOffer, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<JobOffer>> findJobOffersWithFilters(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location) {
        List<JobOffer> jobOffers = jobOfferService.findJobOffersWithFilters(category, location);
        return new ResponseEntity<>(jobOffers, HttpStatus.OK);
    }

}
