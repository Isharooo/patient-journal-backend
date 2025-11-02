package se.kth.lab1.patientjournalbackend.controller;

import se.kth.lab1.patientjournalbackend.dto.DTOMapper;
import se.kth.lab1.patientjournalbackend.dto.PractitionerDTO;
import se.kth.lab1.patientjournalbackend.model.Practitioner;
import se.kth.lab1.patientjournalbackend.service.PractitionerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/practitioners")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class PractitionerController {

    @Autowired
    private PractitionerService practitionerService;

    @Autowired
    private DTOMapper dtoMapper;

    @PostMapping
    public ResponseEntity<PractitionerDTO> createPractitioner(@RequestBody Practitioner practitioner) {
        Practitioner created = practitionerService.createPractitioner(practitioner);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.toPractitionerDTO(created));
    }

    @GetMapping
    public ResponseEntity<List<PractitionerDTO>> getAllPractitioners() {
        List<PractitionerDTO> practitioners = practitionerService.getAllPractitioners().stream()
                .map(dtoMapper::toPractitionerDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(practitioners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPractitionerById(@PathVariable Long id) {
        return practitionerService.getPractitionerById(id)
                .map(practitioner -> ResponseEntity.ok(dtoMapper.toPractitionerDTO(practitioner)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePractitioner(@PathVariable Long id, @RequestBody Practitioner practitioner) {
        try {
            Practitioner updated = practitionerService.updatePractitioner(id, practitioner);
            return ResponseEntity.ok(dtoMapper.toPractitionerDTO(updated));
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