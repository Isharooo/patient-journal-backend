package se.kth.lab1.patientjournalbackend.service;

import se.kth.lab1.patientjournalbackend.model.Practitioner;
import se.kth.lab1.patientjournalbackend.model.User;
import se.kth.lab1.patientjournalbackend.repository.PractitionerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PractitionerService {

    @Autowired
    private PractitionerRepository practitionerRepository;

    @Transactional
    public Practitioner createPractitioner(Practitioner practitioner) {
        Practitioner saved = practitionerRepository.save(practitioner);
        // Force load relations
        if (saved.getUser() != null) {
            saved.getUser().getFirstName();
        }
        if (saved.getOrganization() != null) {
            saved.getOrganization().getName();
        }
        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<Practitioner> getPractitionerById(Long id) {
        Optional<Practitioner> practitioner = practitionerRepository.findById(id);
        practitioner.ifPresent(p -> {
            if (p.getUser() != null) {
                p.getUser().getFirstName();
            }
            if (p.getOrganization() != null) {
                p.getOrganization().getName();
            }
        });
        return practitioner;
    }

    @Transactional(readOnly = true)
    public Optional<Practitioner> getPractitionerByUser(User user) {
        Optional<Practitioner> practitioner = practitionerRepository.findByUser(user);
        practitioner.ifPresent(p -> {
            if (p.getUser() != null) {
                p.getUser().getFirstName();
            }
            if (p.getOrganization() != null) {
                p.getOrganization().getName();
            }
        });
        return practitioner;
    }

    @Transactional(readOnly = true)
    public List<Practitioner> getAllPractitioners() {
        List<Practitioner> practitioners = practitionerRepository.findAll();
        practitioners.forEach(p -> {
            if (p.getUser() != null) {
                p.getUser().getFirstName();
            }
            if (p.getOrganization() != null) {
                p.getOrganization().getName();
            }
        });
        return practitioners;
    }

    @Transactional
    public Practitioner updatePractitioner(Long id, Practitioner updatedPractitioner) {
        Practitioner practitioner = practitionerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Practitioner not found"));

        practitioner.setSpecialization(updatedPractitioner.getSpecialization());
        practitioner.setOrganization(updatedPractitioner.getOrganization());

        Practitioner saved = practitionerRepository.save(practitioner);

        // Force load relations
        if (saved.getUser() != null) {
            saved.getUser().getFirstName();
        }
        if (saved.getOrganization() != null) {
            saved.getOrganization().getName();
        }

        return saved;
    }

    public void deletePractitioner(Long id) {
        practitionerRepository.deleteById(id);
    }
}