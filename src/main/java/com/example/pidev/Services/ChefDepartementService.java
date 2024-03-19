package com.example.pidev.Services;

import com.example.pidev.Entities.ChefDepartement;
import com.example.pidev.Entities.Departement;
import com.example.pidev.Entities.Employe;
import com.example.pidev.Entities.User;
import com.example.pidev.Repositories.ChefDepartementRepository;
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
public class ChefDepartementService {
    @Autowired
    private ChefDepartementRepository chefDepartementRepository;

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmployeRepository employeRepository;

    public ChefDepartement saveChefDepartement(ChefDepartement chefDepartement, Long departementId, Long userId) {
        Departement departement = departementRepository.findById(departementId)
                .orElseThrow(() -> new EntityNotFoundException("Département non trouvé"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User non trouvé"));

        // Définir le chef de département dans le département
        departement.setChefDepartement(chefDepartement);
        chefDepartement.setDepartementDirige(departement);

        // Associer le user avec le chef de département
        chefDepartement.setUser(user);

        // Sauvegarder le chef de département, ce qui devrait également le sauvegarder en tant qu'employé
        ChefDepartement savedChefDepartement = chefDepartementRepository.save(chefDepartement);

        // Mettre à jour le département pour qu'il contienne le chef de département nouvellement ajouté
        departementRepository.save(departement);

        return savedChefDepartement;
    }

    public List<ChefDepartement> findAllChefDepartements() {
        return chefDepartementRepository.findAll();
    }

    public ChefDepartement getChefDepartementById(Long id) {
        return chefDepartementRepository.findById(id).orElse(null);
    }

    public void deleteChefDepartement(Long id) {
        chefDepartementRepository.deleteById(id);
    }
}
