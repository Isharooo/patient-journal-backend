package se.kth.lab1.patientjournalbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "practitioners")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Practitioner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String specialization;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
}