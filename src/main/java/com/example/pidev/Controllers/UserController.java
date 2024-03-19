package com.example.pidev.Controllers;

import com.example.pidev.Entities.User;
import com.example.pidev.Entities.UserCode;
import com.example.pidev.Entities.VerificationToken;
import com.example.pidev.Repositories.UserRepository;
import com.example.pidev.Services.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@AllArgsConstructor
@NoArgsConstructor
public class UserController {

    JavaMailSender mailSender;


    SmsService smsService;
    @Autowired
    UserCodeService codeService;
    @Autowired
    UserService userService;

    @Autowired
    ImplEmailService emailService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    VerificationTokenService verificationTokenService;





    //// Connected User Profile ////////
    @GetMapping("/profile")
    public User getConnectedUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userService.retrieveUserByUsername(username);
        return user;
    }
    @GetMapping("/lastFive")
    public List<User> getLastFiveUsers() {
        return userService.getLastFiveUsers();
    }


    /////// Sign-UP ///////////
    @PostMapping({"/register"})
    public User registerNewUser(@RequestBody User user) {
        User NewUser= userService.registerUser(user);

        VerificationToken verificationToken = verificationTokenService.createVerificationToken(user); // création du jeton de vérification
        verificationTokenService.saveVerificationToken(verificationToken);
        return NewUser;
    }


    @PutMapping("/verify/{verificationToken}")
    public ResponseEntity<User> activateAccount(@PathVariable String verificationToken) throws javax.mail.MessagingException {
        User user = userService.VerifyUser(verificationToken);
        if (user != null) {
            String to = user.getEmail();
            String subject = "Account Created";
            try {
                emailService.sendEmail(to, subject);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    ///////Reset Password Sms////////
    @PostMapping({"/SendSMS/{Phone}"})
    public User SmsSender(@PathVariable Long Phone) {
        User NewUser=userService.retrieveUserByPhone(Phone);
        UserCode userCode = codeService.createVerificationCode(NewUser); // création du jeton de vérification
        codeService.saveVerificationCode(userCode);
        NewUser= userService.updateUser(NewUser);

        return NewUser;
    }


    @PutMapping("/reset-password/{verificationCode}")
    public String activateAccount(@PathVariable String verificationCode,@RequestParam String newPassword ,@RequestParam String confirmPassword) throws javax.mail.MessagingException {
        User user = userService.retrieveByVerificationCode(verificationCode);
        if (user != null) {
            user=userService.ResetPasswordSms(verificationCode,newPassword,confirmPassword);
            String to = user.getEmail();
            String subject = "  Account Updated";
            String text = "Congratulations " + user.getUsername() + " Your password account has been updated successfully";
            try {
                emailService.SendResetMail(to,subject,text);
                user.setVerificationCode(null);
                userService.updateUser(user);

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return ("Congratulations " + user.getUsername() + " Your password account has been updated successfully");
        } else {
            return ("there was an error verifying your account, please make sure you have entered the right token and that the token hasn't expried");
        }
    }



///// Assign Role To User ////////

    @PutMapping  ({"/addRole/{idUser}/{roleName}"})
    public ResponseEntity<User> addRoleToUser(@PathVariable String roleName, @PathVariable Long idUser) {
        User updatedUser = userService.addRoleToUser(roleName, idUser);

        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


//////////////////////////////////////////////////////////////
    @GetMapping("/retrieve-all-Users")
    public List<User> getUsers() {
        List<User> listUsers = userService.retrieveAllUsers();
        return listUsers;

    }
    @GetMapping("/retrieve-user/{iduser}")
    public ResponseEntity<User> getUserDetails(@PathVariable Long iduser) {
        // Add validation to check if userId is a valid Long value
        if (iduser == null) {
            return ResponseEntity.badRequest().build();
        }else
            return ResponseEntity.ok(userService.retrieveUser(iduser));
    }

    @GetMapping("/retrieve/{username}")
    public User getUserByUsername(@PathVariable("username") String username) {
        return userService.retrieveUserByUsername(username);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @DeleteMapping("/remove-user/{iduser}")
    public void removeUser(@PathVariable("iduser") Long IdUser) {
        userService.deleteUser(IdUser);
    }


    @PutMapping("/update-User")
    public User updateUser(@RequestBody User  u) {
        User user = userService.updateUser(u);
        return user;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
}
