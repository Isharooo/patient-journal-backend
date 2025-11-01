package se.kth.lab1.patientjournalbackend.repository;

import se.kth.lab1.patientjournalbackend.model.Practitioner;
import se.kth.lab1.patientjournalbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PractitionerRepository extends JpaRepository<Practitioner, Long> {
    Optional<Practitioner> findByUser(User user);
}