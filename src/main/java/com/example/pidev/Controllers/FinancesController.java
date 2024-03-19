package com.example.pidev.Controllers;

import com.example.pidev.Entities.AnalyseFinanciere;
import com.example.pidev.Entities.Depense;
import com.example.pidev.Entities.ObjectifEpargne;
import com.example.pidev.Services.ServiceGestionFinanciere;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api/finances")
public class FinancesController {

    @Autowired
    private ServiceGestionFinanciere serviceGestionFinanciere;

    @PostMapping("/depenses")
    public ResponseEntity<Depense> ajouterDepense(@RequestParam Long employeId, @RequestBody Depense depense) {
        Depense nouvelleDepense = serviceGestionFinanciere.enregistrerDepense(employeId, depense);
        return ResponseEntity.ok(nouvelleDepense);
    }

    @PostMapping("/objectifs-epargne")
    public ResponseEntity<ObjectifEpargne> ajouterObjectifEpargne(@RequestParam Long employeId, @RequestBody ObjectifEpargne objectif) {
        ObjectifEpargne nouvelObjectif = serviceGestionFinanciere.definirObjectifEpargne(employeId, objectif);
        return ResponseEntity.ok(nouvelObjectif);
    }

    @GetMapping("/analyses/{employeId}")
    public ResponseEntity<AnalyseFinanciere> obtenirAnalyseFinanciere(@PathVariable Long employeId) {
        AnalyseFinanciere analyse = serviceGestionFinanciere.analyserFinances(employeId);
        return ResponseEntity.ok(analyse);
    }

    @GetMapping("/depenses/{employeId}")
    public ResponseEntity<List<Depense>> listerDepenses(@PathVariable Long employeId) {
        List<Depense> depenses = serviceGestionFinanciere.recupererDepenses(employeId);
        return ResponseEntity.ok(depenses);
    }

    @GetMapping("/objectifs-epargne/{employeId}")
    public ResponseEntity<List<ObjectifEpargne>> listerObjectifsEpargne(@PathVariable Long employeId) {
        List<ObjectifEpargne> objectifs = serviceGestionFinanciere.recupererObjectifsEpargne(employeId);
        return ResponseEntity.ok(objectifs);
    }

}
