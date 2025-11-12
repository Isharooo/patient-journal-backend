package se.kth.lab1.patientjournalbackend.controller;

import se.kth.lab1.patientjournalbackend.dto.DTOMapper;
import se.kth.lab1.patientjournalbackend.dto.RegisterRequestDTO;
import se.kth.lab1.patientjournalbackend.dto.UserDTO;
import se.kth.lab1.patientjournalbackend.model.Patient;
import se.kth.lab1.patientjournalbackend.model.User;
import se.kth.lab1.patientjournalbackend.model.UserRole;
import se.kth.lab1.patientjournalbackend.service.PatientService;
import se.kth.lab1.patientjournalbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost"})
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DTOMapper dtoMapper;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequest) { // ÄNDRAD PARAMETER
        try {
            User userToCreate = new User();
            userToCreate.setUsername(registerRequest.getUsername());
            userToCreate.setPassword(registerRequest.getPassword());
            userToCreate.setRole(registerRequest.getRole());
            userToCreate.setFirstName(registerRequest.getFirstName());
            userToCreate.setLastName(registerRequest.getLastName());
            userToCreate.setEmail(registerRequest.getEmail());

            User createdUser = userService.createUser(userToCreate);

            if (createdUser.getRole() == UserRole.PATIENT) {
                if (registerRequest.getPersonalNumber() == null || registerRequest.getDateOfBirth() == null) {
                    throw new RuntimeException("Personal Number and Date of Birth are required for patient registration.");
                }

                Patient newPatient = new Patient();
                newPatient.setUser(createdUser);
                newPatient.setPersonalNumber(registerRequest.getPersonalNumber());
                newPatient.setDateOfBirth(registerRequest.getDateOfBirth());

                patientService.createPatient(newPatient);
            }

            UserDTO userDTO = dtoMapper.toUserDTO(createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        Optional<User> userOpt = userService.getUserByUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        User user = userOpt.get();

        if (!userService.validatePassword(username, password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        UserDTO userDTO = dtoMapper.toUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }
}