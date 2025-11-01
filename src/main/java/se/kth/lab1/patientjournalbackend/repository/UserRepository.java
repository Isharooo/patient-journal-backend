package se.kth.lab1.patientjournalbackend.repository;

import se.kth.lab1.patientjournalbackend.model.User;
import se.kth.lab1.patientjournalbackend.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    List<User> findByRole(UserRole role);
}