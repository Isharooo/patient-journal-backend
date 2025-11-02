package se.kth.lab1.patientjournalbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.kth.lab1.patientjournalbackend.model.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}