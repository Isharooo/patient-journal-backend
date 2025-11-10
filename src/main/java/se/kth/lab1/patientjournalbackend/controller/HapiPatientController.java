package se.kth.lab1.patientjournalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.kth.lab1.patientjournalbackend.dto.PatientDTO;
import se.kth.lab1.patientjournalbackend.dto.HapiFhirMapper;
import se.kth.lab1.patientjournalbackend.service.HapiFhirService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hapi/patients")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost"})
public class HapiPatientController {

    @Autowired
    private HapiFhirService hapiFhirService;

    @Autowired
    private HapiFhirMapper hapiFhirMapper;


    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        try {
            var patients = hapiFhirService.getAllPatients().stream()
                    .map(hapiFhirMapper::toPatientDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(List.of());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable String id) {
        try {
            org.hl7.fhir.r4.model.Patient patient = hapiFhirService.getPatientById(id);
            if (patient == null) {
                return ResponseEntity.notFound().build();
            }
            PatientDTO dto = hapiFhirMapper.toPatientDTO(patient);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/personal-number/{personalNumber}")
    public ResponseEntity<?> getPatientByPersonalNumber(@PathVariable String personalNumber) {
        try {
            org.hl7.fhir.r4.model.Patient patient = hapiFhirService.getPatientByPersonalNumber(personalNumber);
            if (patient == null) {
                return ResponseEntity.notFound().build();
            }
            PatientDTO dto = hapiFhirMapper.toPatientDTO(patient);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}