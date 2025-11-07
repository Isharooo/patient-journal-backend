package se.kth.lab1.patientjournalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.kth.lab1.patientjournalbackend.dto.ConditionDTO;
import se.kth.lab1.patientjournalbackend.dto.HapiFhirMapper;
import se.kth.lab1.patientjournalbackend.service.HapiFhirService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hapi/conditions")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost"})
public class HapiConditionController {

    @Autowired
    private HapiFhirService hapiFhirService;

    @Autowired
    private HapiFhirMapper hapiFhirMapper;

    @GetMapping
    public ResponseEntity<List<ConditionDTO>> getAllConditions() {
        try {
            List<ConditionDTO> conditions = hapiFhirService.getAllConditions().stream()
                    .map(hapiFhirMapper::toConditionDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(conditions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ConditionDTO>> getConditionsByPatientId(@PathVariable String patientId) {
        try {
            List<ConditionDTO> conditions = hapiFhirService.getConditionsByPatientId(patientId).stream()
                    .map(hapiFhirMapper::toConditionDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(conditions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}