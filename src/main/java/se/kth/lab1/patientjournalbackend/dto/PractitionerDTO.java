package se.kth.lab1.patientjournalbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PractitionerDTO {
    private Long id;
    private UserDTO user;
    private String specialization;
    private Long organizationId;
    private String organizationName;
}