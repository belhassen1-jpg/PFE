package com.example.pidev.Controllers;

import com.example.pidev.Entities.Rapport;
import com.example.pidev.Services.RapportService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api/rapports")
public class RapportController {

    @Autowired
    private RapportService rapportService;
    @GetMapping("/{employeId}")
    public ResponseEntity<Rapport> genererOuMettreAJourRapport(@PathVariable Long employeId) {
        try {
            Rapport rapport = rapportService.genererOuMettreAJourRapport(employeId);
            return new ResponseEntity<>(rapport, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Gérer l'exception si l'employé n'est pas trouvé ou toute autre exception
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
