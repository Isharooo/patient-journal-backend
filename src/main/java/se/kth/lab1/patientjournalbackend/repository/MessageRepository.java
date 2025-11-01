package se.kth.lab1.patientjournalbackend.repository;

import se.kth.lab1.patientjournalbackend.model.Message;
import se.kth.lab1.patientjournalbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderOrderBySentAtDesc(User sender);
    List<Message> findByRecipientOrderBySentAtDesc(User recipient);
    List<Message> findByRecipientAndIsReadFalse(User recipient);
    List<Message> findBySenderOrRecipientOrderBySentAtDesc(User sender, User recipient);
}