package se.kth.lab1.patientjournalbackend.controller;

import se.kth.lab1.patientjournalbackend.model.Condition;
import se.kth.lab1.patientjournalbackend.service.ConditionService;
import se.kth.lab1.patientjournalbackend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conditions")
@CrossOrigin(origins = "http://localhost:3000")
public class ConditionController {

    @Autowired
    private ConditionService conditionService;

    @Autowired
    private PatientService patientService;

    @PostMapping
    public ResponseEntity<Condition> createCondition(@RequestBody Condition condition) {
        Condition created = conditionService.createCondition(condition);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Condition>> getAllConditions() {
        return ResponseEntity.ok(conditionService.getAllConditions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getConditionById(@PathVariable Long id) {
        return conditionService.getConditionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getConditionsByPatientId(@PathVariable Long patientId) {
        return patientService.getPatientById(patientId)
                .map(patient -> ResponseEntity.ok(conditionService.getConditionsByPatient(patient)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCondition(@PathVariable Long id, @RequestBody Condition condition) {
        try {
            Condition updated = conditionService.updateCondition(id, condition);
            return ResponseEntity.ok(updated);
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