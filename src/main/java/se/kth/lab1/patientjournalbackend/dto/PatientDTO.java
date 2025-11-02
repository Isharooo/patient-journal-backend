package se.kth.lab1.patientjournalbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Long id;
    private UserDTO user;
    private String personalNumber;
    private LocalDate dateOfBirth;
    private String address;
    private String phoneNumber;
}