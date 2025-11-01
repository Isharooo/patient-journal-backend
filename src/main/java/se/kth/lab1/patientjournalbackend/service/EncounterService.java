package se.kth.lab1.patientjournalbackend.service;

import se.kth.lab1.patientjournalbackend.model.Encounter;
import se.kth.lab1.patientjournalbackend.model.Patient;
import se.kth.lab1.patientjournalbackend.model.Practitioner;
import se.kth.lab1.patientjournalbackend.repository.EncounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EncounterService {

    @Autowired
    private EncounterRepository encounterRepository;

    public Encounter createEncounter(Encounter encounter) {
        return encounterRepository.save(encounter);
    }

    public Optional<Encounter> getEncounterById(Long id) {
        return encounterRepository.findById(id);
    }

    public List<Encounter> getAllEncounters() {
        return encounterRepository.findAll();
    }

    public List<Encounter> getEncountersByPatient(Patient patient) {
        return encounterRepository.findByPatientOrderByEncounterDateDesc(patient);
    }

    public List<Encounter> getEncountersByPractitioner(Practitioner practitioner) {
        return encounterRepository.findByPractitioner(practitioner);
    }

    public Encounter updateEncounter(Long id, Encounter updatedEncounter) {
        Encounter encounter = encounterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Encounter not found"));

        encounter.setNotes(updatedEncounter.getNotes());
        encounter.setLocation(updatedEncounter.getLocation());

        return encounterRepository.save(encounter);
    }

    public void deleteEncounter(Long id) {
        encounterRepository.deleteById(id);
    }
}