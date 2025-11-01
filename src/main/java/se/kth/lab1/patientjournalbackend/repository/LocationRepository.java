package se.kth.lab1.patientjournalbackend.repository;

import se.kth.lab1.patientjournalbackend.model.Location;
import se.kth.lab1.patientjournalbackend.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByOrganization(Organization organization);
}