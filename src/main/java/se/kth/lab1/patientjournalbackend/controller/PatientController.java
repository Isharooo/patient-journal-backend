package se.kth.lab1.patientjournalbackend.controller;

import se.kth.lab1.patientjournalbackend.dto.DTOMapper;
import se.kth.lab1.patientjournalbackend.dto.PatientDTO;
import se.kth.lab1.patientjournalbackend.model.Patient;
import se.kth.lab1.patientjournalbackend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost"})
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private DTOMapper dtoMapper;

    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        try {
            Patient created = patientService.createPatient(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.toPatientDTO(created));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = patientService.getAllPatients().stream()
                .map(dtoMapper::toPatientDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id)
                .map(patient -> ResponseEntity.ok(dtoMapper.toPatientDTO(patient)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/personal-number/{personalNumber}")
    public ResponseEntity<?> getPatientByPersonalNumber(@PathVariable String personalNumber) {
        return patientService.getPatientByPersonalNumber(personalNumber)
                .map(patient -> ResponseEntity.ok(dtoMapper.toPatientDTO(patient)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        try {
            Patient updated = patientService.updatePatient(id, patient);
            return ResponseEntity.ok(dtoMapper.toPatientDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok().build();
    }
}