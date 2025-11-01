package se.kth.lab1.patientjournalbackend.controller;

import se.kth.lab1.patientjournalbackend.model.Practitioner;
import se.kth.lab1.patientjournalbackend.service.PractitionerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/practitioners")
@CrossOrigin(origins = "http://localhost:3000")
public class PractitionerController {

    @Autowired
    private PractitionerService practitionerService;

    @PostMapping
    public ResponseEntity<Practitioner> createPractitioner(@RequestBody Practitioner practitioner) {
        Practitioner created = practitionerService.createPractitioner(practitioner);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Practitioner>> getAllPractitioners() {
        return ResponseEntity.ok(practitionerService.getAllPractitioners());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPractitionerById(@PathVariable Long id) {
        return practitionerService.getPractitionerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePractitioner(@PathVariable Long id, @RequestBody Practitioner practitioner) {
        try {
            Practitioner updated = practitionerService.updatePractitioner(id, practitioner);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePractitioner(@PathVariable Long id) {
        practitionerService.deletePractitioner(id);
        return ResponseEntity.ok().build();
    }
}