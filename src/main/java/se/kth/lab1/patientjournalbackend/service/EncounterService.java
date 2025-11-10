package se.kth.lab1.patientjournalbackend.service;

import se.kth.lab1.patientjournalbackend.model.Encounter;
import se.kth.lab1.patientjournalbackend.model.Patient;
import se.kth.lab1.patientjournalbackend.model.Practitioner;
import se.kth.lab1.patientjournalbackend.repository.EncounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
public class EncounterService {

    @Autowired
    private EncounterRepository encounterRepository;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Encounter createEncounter(Encounter encounter) {
        Encounter saved = encounterRepository.save(encounter);

        entityManager.refresh(saved);

        if (saved.getPatient() != null) {
            saved.getPatient().getUser().getFirstName();
        }
        if (saved.getPractitioner() != null) {
            saved.getPractitioner().getUser().getFirstName();
        }
        if (saved.getLocation() != null) {
            saved.getLocation().getName();
        }

        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<Encounter> getEncounterById(Long id) {
        Optional<Encounter> encounter = encounterRepository.findById(id);
        encounter.ifPresent(e -> {
            if (e.getPatient() != null) {
                e.getPatient().getUser().getFirstName();
            }
            if (e.getPractitioner() != null) {
                e.getPractitioner().getUser().getFirstName();
            }
        });
        return encounter;
    }

    @Transactional(readOnly = true)
    public List<Encounter> getAllEncounters() {
        List<Encounter> encounters = encounterRepository.findAll();
        encounters.forEach(e -> {
            if (e.getPatient() != null) {
                e.getPatient().getUser().getFirstName();
            }
            if (e.getPractitioner() != null) {
                e.getPractitioner().getUser().getFirstName();
            }
        });
        return encounters;
    }

    @Transactional(readOnly = true)
    public List<Encounter> getEncountersByPatient(Patient patient) {
        List<Encounter> encounters = encounterRepository.findByPatientOrderByEncounterDateDesc(patient);
        encounters.forEach(e -> {
            if (e.getPractitioner() != null) {
                e.getPractitioner().getUser().getFirstName();
            }
            if (e.getLocation() != null) {
                e.getLocation().getName();
            }
        });
        return encounters;
    }

    public List<Encounter> getEncountersByPractitioner(Practitioner practitioner) {
        return encounterRepository.findByPractitioner(practitioner);
    }

    @Transactional
    public Encounter updateEncounter(Long id, Encounter updatedEncounter) {
        Encounter encounter = encounterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Encounter not found"));

        encounter.setNotes(updatedEncounter.getNotes());
        encounter.setLocation(updatedEncounter.getLocation());

        Encounter saved = encounterRepository.save(encounter);

        entityManager.refresh(saved);

        if (saved.getPatient() != null) {
            saved.getPatient().getUser().getFirstName();
        }
        if (saved.getPractitioner() != null) {
            saved.getPractitioner().getUser().getFirstName();
        }

        return saved;
    }

    public void deleteEncounter(Long id) {
        encounterRepository.deleteById(id);
    }
}