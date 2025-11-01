package se.kth.lab1.patientjournalbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "encounters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Encounter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "practitioner_id", nullable = false)
    private Practitioner practitioner;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(nullable = false)
    private LocalDateTime encounterDate = LocalDateTime.now();

    @Column(length = 2000)
    private String notes;
}