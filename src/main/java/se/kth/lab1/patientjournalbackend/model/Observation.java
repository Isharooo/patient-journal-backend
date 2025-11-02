package se.kth.lab1.patientjournalbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "observations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Observation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "encounter_id")
    private Encounter encounter;

    @Column(nullable = false)
    private LocalDateTime observationDate = LocalDateTime.now();

    @Column(nullable = false)
    private String observationType;

    @Column(nullable = false)
    private String value;

    private String notes;
}