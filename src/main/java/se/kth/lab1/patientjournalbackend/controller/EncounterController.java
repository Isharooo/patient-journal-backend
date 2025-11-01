package se.kth.lab1.patientjournalbackend.controller;

import se.kth.lab1.patientjournalbackend.model.Encounter;
import se.kth.lab1.patientjournalbackend.service.EncounterService;
import se.kth.lab1.patientjournalbackend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encounters")
@CrossOrigin(origins = "http://localhost:3000")
public class EncounterController {

    @Autowired
    private EncounterService encounterService;

    @Autowired
    private PatientService patientService;

    @PostMapping
    public ResponseEntity<Encounter> createEncounter(@RequestBody Encounter encounter) {
        Encounter created = encounterService.createEncounter(encounter);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Encounter>> getAllEncounters() {
        return ResponseEntity.ok(encounterService.getAllEncounters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEncounterById(@PathVariable Long id) {
        return encounterService.getEncounterById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getEncountersByPatientId(@PathVariable Long patientId) {
        return patientService.getPatientById(patientId)
                .map(patient -> ResponseEntity.ok(encounterService.getEncountersByPatient(patient)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEncounter(@PathVariable Long id, @RequestBody Encounter encounter) {
        try {
            Encounter updated = encounterService.updateEncounter(id, encounter);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEncounter(@PathVariable Long id) {
        encounterService.deleteEncounter(id);
        return ResponseEntity.ok().build();
    }
}