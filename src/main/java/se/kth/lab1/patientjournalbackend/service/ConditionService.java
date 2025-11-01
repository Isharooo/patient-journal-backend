package se.kth.lab1.patientjournalbackend.service;

import se.kth.lab1.patientjournalbackend.model.Condition;
import se.kth.lab1.patientjournalbackend.model.Patient;
import se.kth.lab1.patientjournalbackend.repository.ConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ConditionService {

    @Autowired
    private ConditionRepository conditionRepository;

    public Condition createCondition(Condition condition) {
        return conditionRepository.save(condition);
    }

    public Optional<Condition> getConditionById(Long id) {
        return conditionRepository.findById(id);
    }

    public List<Condition> getAllConditions() {
        return conditionRepository.findAll();
    }

    public List<Condition> getConditionsByPatient(Patient patient) {
        return conditionRepository.findByPatientOrderByDiagnosedDateDesc(patient);
    }

    public Condition updateCondition(Long id, Condition updatedCondition) {
        Condition condition = conditionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Condition not found"));

        condition.setDiagnosisName(updatedCondition.getDiagnosisName());
        condition.setDiagnosisCode(updatedCondition.getDiagnosisCode());
        condition.setNotes(updatedCondition.getNotes());

        return conditionRepository.save(condition);
    }

    public void deleteCondition(Long id) {
        conditionRepository.deleteById(id);
    }
}