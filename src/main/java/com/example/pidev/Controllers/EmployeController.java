package com.example.pidev.Controllers;
import com.example.pidev.Entities.Employe;
import com.example.pidev.Services.EmployeService;
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
@RequestMapping("/api/employes")
public class EmployeController {
    @Autowired
    private EmployeService employeService;


    @PostMapping
    public ResponseEntity<Employe> ajouterEmployeAvecDepartement(
            @RequestBody Employe employe,
            @RequestParam Long departementId,
            @RequestParam(required = false) Long userId) { // userId est facultatif
        Employe nouvelEmploye = employeService.ajouterEmployeAvecDepartement(employe, departementId, userId);
        return new ResponseEntity<>(nouvelEmploye, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Employe>> getAllEmployes() {
        return new ResponseEntity<>(employeService.getAllEmployes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employe> getEmployeById(@PathVariable Long id) {
        return new ResponseEntity<>(employeService.getEmployeById(id), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Employe> updateEmploye(@PathVariable Long id, @RequestBody Employe employe) {
        employe.setEmpId(id);
        return new ResponseEntity<>(employeService.saveOrUpdateEmploye(employe), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        employeService.deleteEmploye(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
