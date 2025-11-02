package se.kth.lab1.patientjournalbackend.controller;

import se.kth.lab1.patientjournalbackend.dto.ConditionDTO;
import se.kth.lab1.patientjournalbackend.dto.DTOMapper;
import se.kth.lab1.patientjournalbackend.model.Condition;
import se.kth.lab1.patientjournalbackend.service.ConditionService;
import se.kth.lab1.patientjournalbackend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/conditions")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class ConditionController {

    @Autowired
    private ConditionService conditionService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DTOMapper dtoMapper;

    @PostMapping
    public ResponseEntity<ConditionDTO> createCondition(@RequestBody Condition condition) {
        Condition created = conditionService.createCondition(condition);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.toConditionDTO(created));
    }

    @GetMapping
    public ResponseEntity<List<ConditionDTO>> getAllConditions() {
        List<ConditionDTO> conditions = conditionService.getAllConditions().stream()
                .map(dtoMapper::toConditionDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(conditions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getConditionById(@PathVariable Long id) {
        return conditionService.getConditionById(id)
                .map(condition -> ResponseEntity.ok(dtoMapper.toConditionDTO(condition)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getConditionsByPatientId(@PathVariable Long patientId) {
        return patientService.getPatientById(patientId)
                .map(patient -> {
                    List<ConditionDTO> conditions = conditionService.getConditionsByPatient(patient).stream()
                            .map(dtoMapper::toConditionDTO)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(conditions);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCondition(@PathVariable Long id, @RequestBody Condition condition) {
        try {
            Condition updated = conditionService.updateCondition(id, condition);
            return ResponseEntity.ok(dtoMapper.toConditionDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCondition(@PathVariable Long id) {
        conditionService.deleteCondition(id);
        return ResponseEntity.ok().build();
    }
}