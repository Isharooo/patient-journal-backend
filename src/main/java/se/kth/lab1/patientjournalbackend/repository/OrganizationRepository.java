package se.kth.lab1.patientjournalbackend.repository;

import se.kth.lab1.patientjournalbackend.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}