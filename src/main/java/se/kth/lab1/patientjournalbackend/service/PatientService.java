package se.kth.lab1.patientjournalbackend.service;

import se.kth.lab1.patientjournalbackend.model.Patient;
import se.kth.lab1.patientjournalbackend.model.User;
import se.kth.lab1.patientjournalbackend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Transactional
    public Patient createPatient(Patient patient) {
        if (patientRepository.existsByPersonalNumber(patient.getPersonalNumber())) {
            throw new RuntimeException("Personal number already exists");
        }
        Patient saved = patientRepository.save(patient);
        // Force load user
        if (saved.getUser() != null) {
            saved.getUser().getFirstName();
        }
        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<Patient> getPatientById(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        patient.ifPresent(p -> {
            if (p.getUser() != null) {
                p.getUser().getFirstName();
            }
        });
        return patient;
    }

    @Transactional(readOnly = true)
    public Optional<Patient> getPatientByUser(User user) {
        Optional<Patient> patient = patientRepository.findByUser(user);
        patient.ifPresent(p -> {
            if (p.getUser() != null) {
                p.getUser().getFirstName();
            }
        });
        return patient;
    }

    @Transactional(readOnly = true)
    public Optional<Patient> getPatientByPersonalNumber(String personalNumber) {
        Optional<Patient> patient = patientRepository.findByPersonalNumber(personalNumber);
        patient.ifPresent(p -> {
            if (p.getUser() != null) {
                p.getUser().getFirstName();
            }
        });
        return patient;
    }

    @Transactional(readOnly = true)
    public List<Patient> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        patients.forEach(p -> {
            if (p.getUser() != null) {
                p.getUser().getFirstName();
            }
        });
        return patients;
    }

    @Transactional
    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setAddress(updatedPatient.getAddress());
        patient.setPhoneNumber(updatedPatient.getPhoneNumber());

        Patient saved = patientRepository.save(patient);

        // Force load user
        if (saved.getUser() != null) {
            saved.getUser().getFirstName();
        }

        return saved;
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}