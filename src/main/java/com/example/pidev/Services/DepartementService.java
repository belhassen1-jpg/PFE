package com.example.pidev.Services;

import com.example.pidev.Entities.Departement;
import com.example.pidev.Entities.Employe;
import com.example.pidev.Repositories.DepartementRepository;
import com.example.pidev.Repositories.EmployeRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
@Service
@AllArgsConstructor
@NoArgsConstructor
public class DepartementService {
    @Autowired
    private DepartementRepository departementRepository;
    @Autowired
    private EmployeRepository employeRepository;

    public List<Departement> listAllDepartements() {
        return departementRepository.findAll();
    }

    public Departement getDepartement(Long id) {
        return departementRepository.findById(id).orElse(null);
    }

    public Departement saveDepartement(Departement departement) {
        return departementRepository.save(departement);
    }

    public void deleteDepartement(Long id) {
        departementRepository.deleteById(id);
    }

    public Employe affecterEmployeAuDepartement(Long employeId, Long departementId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new EntityNotFoundException("Employé non trouvé"));
        Departement departement = departementRepository.findById(departementId)
                .orElseThrow(() -> new EntityNotFoundException("Département non trouvé"));
        employe.setDepartement(departement);
        return employeRepository.save(employe);
    }
}
