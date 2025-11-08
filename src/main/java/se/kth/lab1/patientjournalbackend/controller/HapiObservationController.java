package se.kth.lab1.patientjournalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.kth.lab1.patientjournalbackend.dto.ObservationDTO;
import se.kth.lab1.patientjournalbackend.dto.HapiFhirMapper;
import se.kth.lab1.patientjournalbackend.service.HapiFhirService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hapi/observations")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost"})

public class HapiObservationController {

    @Autowired
    private HapiFhirService hapiFhirService;

    @Autowired
    private HapiFhirMapper hapiFhirMapper;


    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ObservationDTO>> getObservationsByPatientId(@PathVariable String patientId) {
        try {
            List<ObservationDTO> observations = hapiFhirService.getObservationsByPatientId(patientId).stream()
                    .map(hapiFhirMapper::toObservationDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(observations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}