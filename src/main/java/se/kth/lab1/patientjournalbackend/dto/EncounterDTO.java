package se.kth.lab1.patientjournalbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncounterDTO {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long practitionerId;
    private String practitionerName;
    private Long locationId;
    private String locationName;
    private LocalDateTime encounterDate;
    private String notes;
}