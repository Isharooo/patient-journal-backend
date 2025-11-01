package se.kth.lab1.patientjournalbackend.repository;

import se.kth.lab1.patientjournalbackend.model.Condition;
import se.kth.lab1.patientjournalbackend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {
    List<Condition> findByPatient(Patient patient);
    List<Condition> findByPatientOrderByDiagnosedDateDesc(Patient patient);
}