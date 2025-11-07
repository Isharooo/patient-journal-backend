package se.kth.lab1.patientjournalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.kth.lab1.patientjournalbackend.dto.EncounterDTO;
import se.kth.lab1.patientjournalbackend.dto.HapiFhirMapper;
import se.kth.lab1.patientjournalbackend.service.HapiFhirService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hapi/encounters")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost"})
public class HapiEncounterController {

    @Autowired
    private HapiFhirService hapiFhirService;

    @Autowired
    private HapiFhirMapper hapiFhirMapper;

    @GetMapping
    public ResponseEntity<List<EncounterDTO>> getAllEncounters() {
        try {
            List<EncounterDTO> encounters = hapiFhirService.getAllEncounters().stream()
                    .map(hapiFhirMapper::toEncounterDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(encounters);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<EncounterDTO>> getEncountersByPatientId(@PathVariable String patientId) {
        try {
            List<EncounterDTO> encounters = hapiFhirService.getEncountersByPatientId(patientId).stream()
                    .map(hapiFhirMapper::toEncounterDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(encounters);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}