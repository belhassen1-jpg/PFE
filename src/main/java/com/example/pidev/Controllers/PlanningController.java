package com.example.pidev.Controllers;

import com.example.pidev.Entities.FeuilleTemps;
import com.example.pidev.Entities.Planning;
import com.example.pidev.Repositories.FeuilleTempsRepository;
import com.example.pidev.Services.PlanningService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api/plannings")
public class PlanningController {
    @Autowired
    private PlanningService planningService;
    @Autowired
    private FeuilleTempsRepository feuilleTempsRepository;

    @PostMapping("/employe")
    public ResponseEntity<Planning> creerPlanning(@RequestBody Planning planning) {
        Planning createdPlanning = planningService.creerPlanning(planning);
        return new ResponseEntity<>(createdPlanning, HttpStatus.CREATED);
    }

    @PostMapping("/feuilleTemps/employe/{employeId}/planning/{planningId}")
    public ResponseEntity<FeuilleTemps> creerEtAssocierFeuilleTemps(
            @PathVariable Long employeId,
            @PathVariable Long planningId,
            @RequestBody FeuilleTemps feuilleTemps) {

        FeuilleTemps createdFeuilleTemps = planningService.creerEtAssocierFeuilleTemps(employeId, planningId, feuilleTemps);
        return new ResponseEntity<>(createdFeuilleTemps, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planning> obtenirPlanning(@PathVariable Long id) {
        Planning planning = planningService.obtenirPlanning(id);
        return new ResponseEntity<>(planning, HttpStatus.OK);
    }


    @GetMapping("/feuillestemps")
    public ResponseEntity<List<FeuilleTemps>> obtenirFeuillesDeTemps(@RequestParam(required = false) Long planningId,
                                                                     @RequestParam(required = false) Long employeId) {
        List<FeuilleTemps> feuillesDeTemps = planningService.obtenirFeuillesDeTemps(planningId, employeId);
        return new ResponseEntity<>(feuillesDeTemps, HttpStatus.OK);
    }

    @PutMapping("/feuillestemps/{feuilleTempsId}/approuver")
    public ResponseEntity<FeuilleTemps> approuverFeuilleDeTemps(@PathVariable Long feuilleTempsId,
                                                                @RequestParam boolean estApprouve) {
        FeuilleTemps updatedFeuilleTemps = planningService.changerApprobationFeuilleTemps(feuilleTempsId, estApprouve);
        return new ResponseEntity<>(updatedFeuilleTemps, HttpStatus.OK);
    }



    @GetMapping("/{employeId}/total-heures-travaillees")
    public ResponseEntity<Long> calculerTotalHeuresTravailleesPourEmploye(@PathVariable Long employeId) {
        Long totalHeures = planningService.calculerTotalHeuresTravailleesPourEmploye(employeId);
        return new ResponseEntity<>(totalHeures, HttpStatus.OK);
    }
}
