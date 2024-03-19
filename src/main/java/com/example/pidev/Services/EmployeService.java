package com.example.pidev.Services;

import com.example.pidev.Entities.ChefDepartement;
import com.example.pidev.Entities.Departement;
import com.example.pidev.Entities.Employe;
import com.example.pidev.Entities.User;
import com.example.pidev.Repositories.DepartementRepository;
import com.example.pidev.Repositories.EmployeRepository;
import com.example.pidev.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
@Service
@AllArgsConstructor
@NoArgsConstructor
public class EmployeService {
    @Autowired
    private EmployeRepository employeRepository;
    @Autowired
    private DepartementRepository departementRepository;
    @Autowired
    private UserRepository userRepository;

    public Employe ajouterEmployeAvecDepartement(Employe employe, Long departementId, Long userId) {
        // Trouver le département par son ID
        Departement departement = departementRepository.findById(departementId)
                .orElseThrow(() -> new EntityNotFoundException("Département non trouvé"));

        // Récupérer l'utilisateur ou le créer si l'ID est fourni
        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        } else if (employe.getUser() != null) {
            user = userRepository.save(employe.getUser());
        }

        // Créer un nouvel Employe avec les détails fournis
        Employe nouvelEmploye = new Employe();
        BeanUtils.copyProperties(employe, nouvelEmploye, "empId");
        nouvelEmploye.setDepartement(departement);
        nouvelEmploye.setUser(user); // Associer l'utilisateur

        // Sauvegarder le nouvel Employe
        return employeRepository.save(nouvelEmploye);
    }

    public List<Employe> getAllEmployes() {
        return employeRepository.findAll();
    }

    public Employe getEmployeById(Long id) {
        return employeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employé non trouvé"));
    }

    public Employe saveOrUpdateEmploye(Employe employe) {
        return employeRepository.save(employe);
    }

    public void deleteEmploye(Long id) {
        employeRepository.deleteById(id);
    }
}
