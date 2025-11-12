package se.kth.lab1.patientjournalbackend.dto;

import lombok.Data;
import se.kth.lab1.patientjournalbackend.model.UserRole;
import java.time.LocalDate;

@Data
public class RegisterRequestDTO {
    private String username;
    private String password;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String email;

    private String personalNumber;
    private LocalDate dateOfBirth;
}