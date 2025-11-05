package se.kth.lab1.patientjournalbackend.service;

import se.kth.lab1.patientjournalbackend.model.Observation;
import se.kth.lab1.patientjournalbackend.model.Patient;
import se.kth.lab1.patientjournalbackend.model.Encounter;
import se.kth.lab1.patientjournalbackend.repository.ObservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
public class ObservationService {

    @Autowired
    private ObservationRepository observationRepository;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Observation createObservation(Observation observation) {
        Observation saved = observationRepository.save(observation);

        // Refresh to load all relationships
        entityManager.refresh(saved);

        // Force load relations
        if (saved.getPatient() != null) {
            saved.getPatient().getUser().getFirstName();
        }
        if (saved.getEncounter() != null) {
            saved.getEncounter().getNotes();
        }

        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<Observation> getObservationById(Long id) {
        Optional<Observation> observation = observationRepository.findById(id);
        observation.ifPresent(o -> {
            if (o.getPatient() != null) {
                o.getPatient().getUser().getFirstName();
            }
        });
        return observation;
    }

    @Transactional(readOnly = true)
    public List<Observation> getAllObservations() {
        List<Observation> observations = observationRepository.findAll();
        observations.forEach(o -> {
            if (o.getPatient() != null) {
                o.getPatient().getUser().getFirstName();
            }
        });
        return observations;
    }

    @Transactional(readOnly = true)
    public List<Observation> getObservationsByPatient(Patient patient) {
        List<Observation> observations = observationRepository.findByPatientOrderByObservationDateDesc(patient);
        observations.forEach(o -> {
            if (o.getEncounter() != null) {
                o.getEncounter().getNotes();
            }
        });
        return observations;
    }

    @Transactional(readOnly = true)
    public List<Observation> getObservationsByEncounter(Encounter encounter) {
        return observationRepository.findByEncounter(encounter);
    }

    @Transactional
    public Observation updateObservation(Long id, Observation updatedObservation) {
        Observation observation = observationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Observation not found"));

        observation.setObservationType(updatedObservation.getObservationType());
        observation.setValue(updatedObservation.getValue());
        observation.setNotes(updatedObservation.getNotes());

        Observation saved = observationRepository.save(observation);

        // Refresh to load all relationships
        entityManager.refresh(saved);

        // Force load relations
        if (saved.getPatient() != null) {
            saved.getPatient().getUser().getFirstName();
        }

        return saved;
    }

    public void deleteObservation(Long id) {
        observationRepository.deleteById(id);
    }
}