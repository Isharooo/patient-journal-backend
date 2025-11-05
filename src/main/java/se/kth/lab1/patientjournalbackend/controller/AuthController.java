package se.kth.lab1.patientjournalbackend.controller;

import se.kth.lab1.patientjournalbackend.dto.DTOMapper;
import se.kth.lab1.patientjournalbackend.dto.UserDTO;
import se.kth.lab1.patientjournalbackend.model.User;
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
    private DTOMapper dtoMapper;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
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