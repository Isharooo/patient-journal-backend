package se.kth.lab1.patientjournalbackend.service;

import se.kth.lab1.patientjournalbackend.model.Observation;
import se.kth.lab1.patientjournalbackend.model.Patient;
import se.kth.lab1.patientjournalbackend.model.Encounter;
import se.kth.lab1.patientjournalbackend.repository.ObservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ObservationService {

    @Autowired
    private ObservationRepository observationRepository;

    public Observation createObservation(Observation observation) {
        return observationRepository.save(observation);
    }

    public Optional<Observation> getObservationById(Long id) {
        return observationRepository.findById(id);
    }

    public List<Observation> getAllObservations() {
        return observationRepository.findAll();
    }

    public List<Observation> getObservationsByPatient(Patient patient) {
        return observationRepository.findByPatientOrderByObservationDateDesc(patient);
    }

    public List<Observation> getObservationsByEncounter(Encounter encounter) {
        return observationRepository.findByEncounter(encounter);
    }

    public Observation updateObservation(Long id, Observation updatedObservation) {
        Observation observation = observationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Observation not found"));

        observation.setObservationType(updatedObservation.getObservationType());
        observation.setValue(updatedObservation.getValue());
        observation.setNotes(updatedObservation.getNotes());

        return observationRepository.save(observation);
    }

    public void deleteObservation(Long id) {
        observationRepository.deleteById(id);
    }
}