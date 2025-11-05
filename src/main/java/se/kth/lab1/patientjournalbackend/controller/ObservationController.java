package se.kth.lab1.patientjournalbackend.controller;

import se.kth.lab1.patientjournalbackend.dto.DTOMapper;
import se.kth.lab1.patientjournalbackend.dto.ObservationDTO;
import se.kth.lab1.patientjournalbackend.model.Observation;
import se.kth.lab1.patientjournalbackend.service.ObservationService;
import se.kth.lab1.patientjournalbackend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/observations")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost"})
public class ObservationController {

    @Autowired
    private ObservationService observationService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DTOMapper dtoMapper;

    @PostMapping
    public ResponseEntity<ObservationDTO> createObservation(@RequestBody Observation observation) {
        Observation created = observationService.createObservation(observation);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.toObservationDTO(created));
    }

    @GetMapping
    public ResponseEntity<List<ObservationDTO>> getAllObservations() {
        List<ObservationDTO> observations = observationService.getAllObservations().stream()
                .map(dtoMapper::toObservationDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(observations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getObservationById(@PathVariable Long id) {
        return observationService.getObservationById(id)
                .map(observation -> ResponseEntity.ok(dtoMapper.toObservationDTO(observation)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getObservationsByPatientId(@PathVariable Long patientId) {
        return patientService.getPatientById(patientId)
                .map(patient -> {
                    List<ObservationDTO> observations = observationService.getObservationsByPatient(patient).stream()
                            .map(dtoMapper::toObservationDTO)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(observations);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateObservation(@PathVariable Long id, @RequestBody Observation observation) {
        try {
            Observation updated = observationService.updateObservation(id, observation);
            return ResponseEntity.ok(dtoMapper.toObservationDTO(updated));
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