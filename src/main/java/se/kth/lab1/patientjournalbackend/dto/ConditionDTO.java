package se.kth.lab1.patientjournalbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConditionDTO {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long encounterId;
    private Long diagnosedById;
    private String diagnosedByName;
    private String diagnosisCode;
    private String diagnosisName;
    private String notes;
    private LocalDateTime diagnosedDate;
}