package se.kth.lab1.patientjournalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.kth.lab1.patientjournalbackend.dto.PractitionerDTO;
import se.kth.lab1.patientjournalbackend.dto.HapiFhirMapper;
import se.kth.lab1.patientjournalbackend.service.HapiFhirService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hapi/practitioners")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost"})
public class HapiPractitionerController {

    @Autowired
    private HapiFhirService hapiFhirService;

    @Autowired
    private HapiFhirMapper hapiFhirMapper;

    @GetMapping
    public ResponseEntity<List<PractitionerDTO>> getAllPractitioners() {
        try {
            List<PractitionerDTO> practitioners = hapiFhirService.getAllPractitioners().stream()
                    .map(hapiFhirMapper::toPractitionerDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(practitioners);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPractitionerById(@PathVariable String id) {
        try {
            org.hl7.fhir.r4.model.Practitioner practitioner = hapiFhirService.getPractitionerById(id);
            if (practitioner == null) {
                return ResponseEntity.notFound().build();
            }
            PractitionerDTO dto = hapiFhirMapper.toPractitionerDTO(practitioner);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}