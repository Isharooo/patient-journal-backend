package se.kth.lab1.patientjournalbackend.controller;

import se.kth.lab1.patientjournalbackend.model.Organization;
import se.kth.lab1.patientjournalbackend.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost"})
public class OrganizationController {

    @Autowired
    private OrganizationRepository organizationRepository;

    @PostMapping
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization organization) {
        Organization created = organizationRepository.save(organization);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        return ResponseEntity.ok(organizationRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrganizationById(@PathVariable Long id) {
        return organizationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}