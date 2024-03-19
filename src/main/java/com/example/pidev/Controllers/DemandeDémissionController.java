package com.example.pidev.Controllers;

import com.example.pidev.Entities.DemandeDémission;
import com.example.pidev.Services.DemandeDémissionService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api/demissions")
public class DemandeDémissionController {
    @Autowired
    private DemandeDémissionService demandeDémissionService;

    @PostMapping("/creer")
    public ResponseEntity<DemandeDémission> creerDemandeDémission(@RequestBody DemandeDémission demande) {
        DemandeDémission nouvelleDemande = demandeDémissionService.creerDemandeDémission(demande);
        return new ResponseEntity<>(nouvelleDemande, HttpStatus.CREATED);
    }

    @PostMapping("/traiter/{demandeId}")
    public ResponseEntity<DemandeDémission> traiterDemandeDémission(@PathVariable Long demandeId, @RequestParam boolean accepter) {
        DemandeDémission demande = demandeDémissionService.traiterDemandeDémission(demandeId, accepter);
        return new ResponseEntity<>(demande, HttpStatus.OK);
    }
}
