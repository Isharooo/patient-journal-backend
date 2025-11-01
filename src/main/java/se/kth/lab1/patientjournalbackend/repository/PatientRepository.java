package se.kth.lab1.patientjournalbackend.repository;

import se.kth.lab1.patientjournalbackend.model.Patient;
import se.kth.lab1.patientjournalbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser(User user);
    Optional<Patient> findByPersonalNumber(String personalNumber);
    boolean existsByPersonalNumber(String personalNumber);
}