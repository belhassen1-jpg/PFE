package com.example.pidev.Controllers;

import com.example.pidev.Entities.Partenaire;
import com.example.pidev.Services.PartenaireService;
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
@RequestMapping("/api/partenaires")
public class PartenaireController {
    @Autowired
    private PartenaireService partenaireService;

    @GetMapping
    public ResponseEntity<List<Partenaire>> getAllPartenaires() {
        return new ResponseEntity<>(partenaireService.findAllPartenaires(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partenaire> getPartenaireById(@PathVariable Long id) {
        return partenaireService.findPartenaireById(id)
                .map(partenaire -> new ResponseEntity<>(partenaire, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Partenaire> createPartenaire(@RequestBody Partenaire partenaire) {
        Partenaire newPartenaire = partenaireService.savePartenaire(partenaire);
        return new ResponseEntity<>(newPartenaire, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partenaire> updatePartenaire(@PathVariable Long id, @RequestBody Partenaire partenaire) {
        return partenaireService.findPartenaireById(id)
                .map(partenaireToUpdate -> {
                    partenaire.setId(id);
                    return new ResponseEntity<>(partenaireService.savePartenaire(partenaire), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartenaire(@PathVariable Long id) {
        partenaireService.deletePartenaire(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
