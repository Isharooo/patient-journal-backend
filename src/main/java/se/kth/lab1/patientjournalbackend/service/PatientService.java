package se.kth.lab1.patientjournalbackend.service;

import se.kth.lab1.patientjournalbackend.model.Patient;
import se.kth.lab1.patientjournalbackend.model.User;
import se.kth.lab1.patientjournalbackend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient createPatient(Patient patient) {
        if (patientRepository.existsByPersonalNumber(patient.getPersonalNumber())) {
            throw new RuntimeException("Personal number already exists");
        }
        return patientRepository.save(patient);
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Optional<Patient> getPatientByUser(User user) {
        return patientRepository.findByUser(user);
    }

    public Optional<Patient> getPatientByPersonalNumber(String personalNumber) {
        return patientRepository.findByPersonalNumber(personalNumber);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setAddress(updatedPatient.getAddress());
        patient.setPhoneNumber(updatedPatient.getPhoneNumber());

        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}