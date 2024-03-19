package com.example.pidev.Controllers;

import com.example.pidev.Entities.BulletinPaie;
import com.example.pidev.Entities.DeclarationFiscale;
import com.example.pidev.Entities.Employe;
import com.example.pidev.Entities.Paie;
import com.example.pidev.Services.ServicePaie;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api/paie")
public class PaieController {

    @Autowired
    private ServicePaie servicePaie;

    @PostMapping("/calculer/{employeId}")
    public ResponseEntity<Paie> calculerEtSauvegarderPaie(@PathVariable Long employeId) {
        Paie paie = servicePaie.calculerPaie(employeId);
        return new ResponseEntity<>(paie, HttpStatus.CREATED);
    }

    @PostMapping("/bulletin/{employeId}")
    public ResponseEntity<?> genererBulletinPaie(@PathVariable Long employeId) {
        try {
            BulletinPaie bulletin = servicePaie.genererBulletinPaie(employeId);
            return ResponseEntity.ok(bulletin);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/declaration/{employeId}")
    public ResponseEntity<DeclarationFiscale> genererDeclarationFiscale(@PathVariable Long employeId) {
        Employe employe = servicePaie.obtenirDernierePaie(employeId).getEmploye();
        DeclarationFiscale declarationFiscale = servicePaie.genererDeclarationFiscale(employe);
        return ResponseEntity.ok(declarationFiscale);
    }

    @GetMapping("/bulletins/{employeId}")
    public ResponseEntity<List<BulletinPaie>> obtenirBulletinsPaie(@PathVariable Long employeId) {
        List<BulletinPaie> bulletins = servicePaie.obtenirBulletinsPaie(employeId);
        return ResponseEntity.ok(bulletins);
    }

    @GetMapping("/declarations/{employeId}")
    public ResponseEntity<List<DeclarationFiscale>> obtenirDeclarationsFiscales(@PathVariable Long employeId) {
        List<DeclarationFiscale> declarations = servicePaie.obtenirDeclarationsFiscales(employeId);
        return ResponseEntity.ok(declarations);
    }

    @GetMapping("/dernierePaie/{employeId}")
    public ResponseEntity<Paie> obtenirDernierePaie(@PathVariable Long employeId) {
        Paie dernierePaie = servicePaie.obtenirDernierePaie(employeId);
        if (dernierePaie != null) {
            return ResponseEntity.ok(dernierePaie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/dernierBulletinPaie/{employeId}")
    public ResponseEntity<BulletinPaie> obtenirDernierBulletinPaie(@PathVariable Long employeId) {
        BulletinPaie dernierBulletin = servicePaie.obtenirDernierBulletinPaie(employeId);
        return ResponseEntity.ok(dernierBulletin);
    }

}
