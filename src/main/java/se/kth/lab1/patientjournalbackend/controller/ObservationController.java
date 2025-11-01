package se.kth.lab1.patientjournalbackend.controller;

import se.kth.lab1.patientjournalbackend.model.Observation;
import se.kth.lab1.patientjournalbackend.service.ObservationService;
import se.kth.lab1.patientjournalbackend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/observations")
@CrossOrigin(origins = "http://localhost:3000")
public class ObservationController {

    @Autowired
    private ObservationService observationService;

    @Autowired
    private PatientService patientService;

    @PostMapping
    public ResponseEntity<Observation> createObservation(@RequestBody Observation observation) {
        Observation created = observationService.createObservation(observation);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Observation>> getAllObservations() {
        return ResponseEntity.ok(observationService.getAllObservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getObservationById(@PathVariable Long id) {
        return observationService.getObservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getObservationsByPatientId(@PathVariable Long patientId) {
        return patientService.getPatientById(patientId)
                .map(patient -> ResponseEntity.ok(observationService.getObservationsByPatient(patient)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateObservation(@PathVariable Long id, @RequestBody Observation observation) {
        try {
            Observation updated = observationService.updateObservation(id, observation);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteObservation(@PathVariable Long id) {
        observationService.deleteObservation(id);
        return ResponseEntity.ok().build();
    }
}