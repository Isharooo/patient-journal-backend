package se.kth.lab1.patientjournalbackend.controller;

import se.kth.lab1.patientjournalbackend.dto.DTOMapper;
import se.kth.lab1.patientjournalbackend.dto.EncounterDTO;
import se.kth.lab1.patientjournalbackend.model.Encounter;
import se.kth.lab1.patientjournalbackend.service.EncounterService;
import se.kth.lab1.patientjournalbackend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/encounters")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost"})
public class EncounterController {

    @Autowired
    private EncounterService encounterService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DTOMapper dtoMapper;

    @PostMapping
    public ResponseEntity<EncounterDTO> createEncounter(@RequestBody Encounter encounter) {
        Encounter created = encounterService.createEncounter(encounter);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.toEncounterDTO(created));
    }

    @GetMapping
    public ResponseEntity<List<EncounterDTO>> getAllEncounters() {
        List<EncounterDTO> encounters = encounterService.getAllEncounters().stream()
                .map(dtoMapper::toEncounterDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(encounters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEncounterById(@PathVariable Long id) {
        return encounterService.getEncounterById(id)
                .map(encounter -> ResponseEntity.ok(dtoMapper.toEncounterDTO(encounter)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getEncountersByPatientId(@PathVariable Long patientId) {
        return patientService.getPatientById(patientId)
                .map(patient -> {
                    List<EncounterDTO> encounters = encounterService.getEncountersByPatient(patient).stream()
                            .map(dtoMapper::toEncounterDTO)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(encounters);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEncounter(@PathVariable Long id, @RequestBody Encounter encounter) {
        try {
            Encounter updated = encounterService.updateEncounter(id, encounter);
            return ResponseEntity.ok(dtoMapper.toEncounterDTO(updated));
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