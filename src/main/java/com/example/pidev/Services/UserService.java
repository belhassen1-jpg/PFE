package com.example.pidev.Services;
import com.example.pidev.Entities.*;
import com.example.pidev.Interfaces.IUserService;
import com.example.pidev.Repositories.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;


@Service
@AllArgsConstructor
@NoArgsConstructor

public class UserService implements IUserService {


    @Autowired
    ImplEmailService emailService;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserCodeRepository userCodeRepository;

    @Override
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public List<User> getLastFiveUsers() {
        return userRepository.findTop5ByOrderByCreatedATDesc();
    }
    @Override
    public User addUser(User user) {

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User retrieveUser(Long IdUser) {
        return userRepository.findById(IdUser).get();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User retrieveUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public User retrieveByVerificationCode(String code) {
        return userRepository.findByVerificationCode(code);
    }

    ////////SIGN-UP/////////
    @Override
    public User registerUser(User user) {


        Role role = roleRepository.findRoleByRoleName("Trader");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        user.setRoles(userRoles);
        user.setIsVerified(0);
        user.setPassword(getEncodedPassword(user.getPassword()));
        Date dateNaissance = user.getBirthDate();
        LocalDate localDateNaissance = dateNaissance.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateSysteme = LocalDate.now();
        int age = Period.between(localDateNaissance, dateSysteme).getYears();
        user.setAge(age);

        Map<String, Object> params = new HashMap<>();
        params.put("name","ahmed");

        try {
            userRepository.save(user);
            emailService.sendVerificationEmail(user);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public User retrieveUserByPhone(Long Phone) {
        return userRepository.findByPhone(Phone);
    }


    //////User Verification //////
    public User VerifyUser(String token) {
        User user = userRepository.findByVerificationToken(token);
        if (user != null) {
            user.setIsVerified(1);
            user.setVerificationToken(null);
            userRepository.save(user);
        }
        return user;
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }


    ///// AssignRole ///
    public User addRoleToUser(String roleName, Long idUser) {
        Role r = roleRepository.findRoleByRoleName(roleName);
        User u = userRepository.findById(idUser).orElse(null);

        if (r != null && u != null) {
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(r);

            u.setRoles(userRoles);
            userRepository.save(u);

            return u; // Return the updated user with the new role
        }

        return null; // Return null if user or role is not found
    }



    public User ResetPasswordSms(String code, String newPassword, String confirmPassword) {
        User user = userRepository.findByVerificationCode(code);
        UserCode userCode = userCodeRepository.findByCode(code);
        if (user != null) {
            if (user.getVerificationCode().equals(userCode.getCode())) {
                if (newPassword.equals(confirmPassword)) {
                    user.setPassword(passwordEncoder.encode(newPassword));

                    userRepository.save(user);
                } else {
                    throw new IllegalArgumentException("Passwords do not match");
                }
            } else {
                throw new IllegalArgumentException("User not found");
            }
        } else {
            throw new IllegalArgumentException("Verification code is invalid");
        }
        return user;

    }




    public List<String> getAllUserEmails() {
        List<User> users = userRepository.findAll(); // Récupérez tous les utilisateurs depuis la base de données

        List<String> emails = new ArrayList<>();

        for (User user : users) {
            if (user.getEmail() != null) {
                emails.add(user.getEmail()); // Ajoutez l'e-mail de l'utilisateur à la liste
            }
        }

        return emails; // Déplacez cette ligne à l'extérieur de la boucle
    }
}





