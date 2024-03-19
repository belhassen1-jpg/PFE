package com.example.pidev.Controllers;

import com.example.pidev.Entities.Departement;
import com.example.pidev.Entities.Employe;
import com.example.pidev.Services.DepartementService;
import com.example.pidev.Services.EmployeService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api/departements")
public class DepartementController {
    @Autowired
    private DepartementService departementService;
    @Autowired
    private  EmployeService employeService;

    @GetMapping
    public List<Departement> getAllDepartements() {
        return departementService.listAllDepartements();
    }

    @PostMapping
    public Departement createDepartement(@RequestBody Departement departement) {
        return departementService.saveDepartement(departement);
    }

    @GetMapping("/{id}")
    public Departement getDepartement(@PathVariable Long id) {
        return departementService.getDepartement(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDepartement(@PathVariable Long id) {
        departementService.deleteDepartement(id);
    }


    @PostMapping("/{departementId}/employe/{employeId}/affecter")
    public Employe affecterEmployeAuDepartement(@PathVariable Long departementId, @PathVariable Long employeId) {
        Employe employe = employeService.getEmployeById(employeId); // Utiliser EmployeService ici
        if (employe.getDepartement() != null && !employe.getDepartement().getId().equals(departementId)) {
            throw new IllegalStateException("L'employé est déjà affecté à un autre département");
        }
        return departementService.affecterEmployeAuDepartement(employeId, departementId);
    }
}
