package se.kth.lab1.patientjournalbackend.service;

import se.kth.lab1.patientjournalbackend.model.Practitioner;
import se.kth.lab1.patientjournalbackend.model.User;
import se.kth.lab1.patientjournalbackend.repository.PractitionerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PractitionerService {

    @Autowired
    private PractitionerRepository practitionerRepository;

    public Practitioner createPractitioner(Practitioner practitioner) {
        return practitionerRepository.save(practitioner);
    }

    public Optional<Practitioner> getPractitionerById(Long id) {
        return practitionerRepository.findById(id);
    }

    public Optional<Practitioner> getPractitionerByUser(User user) {
        return practitionerRepository.findByUser(user);
    }

    public List<Practitioner> getAllPractitioners() {
        return practitionerRepository.findAll();
    }

    public Practitioner updatePractitioner(Long id, Practitioner updatedPractitioner) {
        Practitioner practitioner = practitionerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Practitioner not found"));

        practitioner.setSpecialization(updatedPractitioner.getSpecialization());
        practitioner.setOrganization(updatedPractitioner.getOrganization());

        return practitionerRepository.save(practitioner);
    }

    public void deletePractitioner(Long id) {
        practitionerRepository.deleteById(id);
    }
}