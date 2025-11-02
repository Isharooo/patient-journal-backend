package se.kth.lab1.patientjournalbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObservationDTO {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long encounterId;
    private LocalDateTime observationDate;
    private String observationType;
    private String value;
    private String notes;
}