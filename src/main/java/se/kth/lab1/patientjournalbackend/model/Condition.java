package se.kth.lab1.patientjournalbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "conditions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "encounter_id")
    private Encounter encounter;

    @ManyToOne
    @JoinColumn(name = "diagnosed_by_id", nullable = false)
    private Practitioner diagnosedBy;

    @Column(nullable = false)
    private String diagnosisCode;

    @Column(nullable = false)
    private String diagnosisName;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false)
    private LocalDateTime diagnosedDate = LocalDateTime.now();
}