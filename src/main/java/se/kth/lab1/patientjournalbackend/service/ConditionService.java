package se.kth.lab1.patientjournalbackend.service;

import se.kth.lab1.patientjournalbackend.model.Condition;
import se.kth.lab1.patientjournalbackend.model.Patient;
import se.kth.lab1.patientjournalbackend.repository.ConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
public class ConditionService {

    @Autowired
    private ConditionRepository conditionRepository;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Condition createCondition(Condition condition) {
        Condition saved = conditionRepository.save(condition);

        entityManager.refresh(saved);

        if (saved.getPatient() != null) {
            saved.getPatient().getUser().getFirstName();
        }
        if (saved.getDiagnosedBy() != null) {
            saved.getDiagnosedBy().getUser().getFirstName();
        }
        if (saved.getEncounter() != null) {
            saved.getEncounter().getNotes();
        }

        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<Condition> getConditionById(Long id) {
        Optional<Condition> condition = conditionRepository.findById(id);
        condition.ifPresent(c -> {
            if (c.getPatient() != null) {
                c.getPatient().getUser().getFirstName();
            }
            if (c.getDiagnosedBy() != null) {
                c.getDiagnosedBy().getUser().getFirstName();
            }
        });
        return condition;
    }

    @Transactional(readOnly = true)
    public List<Condition> getAllConditions() {
        List<Condition> conditions = conditionRepository.findAll();
        conditions.forEach(c -> {
            if (c.getPatient() != null) {
                c.getPatient().getUser().getFirstName();
            }
            if (c.getDiagnosedBy() != null) {
                c.getDiagnosedBy().getUser().getFirstName();
            }
        });
        return conditions;
    }

    @Transactional(readOnly = true)
    public List<Condition> getConditionsByPatient(Patient patient) {
        List<Condition> conditions = conditionRepository.findByPatientOrderByDiagnosedDateDesc(patient);
        conditions.forEach(c -> {
            if (c.getDiagnosedBy() != null) {
                c.getDiagnosedBy().getUser().getFirstName();
            }
            if (c.getEncounter() != null) {
                c.getEncounter().getNotes();
            }
        });
        return conditions;
    }

    @Transactional
    public Condition updateCondition(Long id, Condition updatedCondition) {
        Condition condition = conditionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Condition not found"));

        condition.setDiagnosisName(updatedCondition.getDiagnosisName());
        condition.setDiagnosisCode(updatedCondition.getDiagnosisCode());
        condition.setNotes(updatedCondition.getNotes());

        Condition saved = conditionRepository.save(condition);

        entityManager.refresh(saved);

        if (saved.getPatient() != null) {
            saved.getPatient().getUser().getFirstName();
        }
        if (saved.getDiagnosedBy() != null) {
            saved.getDiagnosedBy().getUser().getFirstName();
        }

        return saved;
    }

    public void deleteCondition(Long id) {
        conditionRepository.deleteById(id);
    }
}