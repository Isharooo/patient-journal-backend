package se.kth.lab1.patientjournalbackend.service;

import se.kth.lab1.patientjournalbackend.model.Message;
import se.kth.lab1.patientjournalbackend.model.User;
import se.kth.lab1.patientjournalbackend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    public List<Message> getMessagesBySender(User sender) {
        return messageRepository.findBySenderOrderBySentAtDesc(sender);
    }

    public List<Message> getMessagesByRecipient(User recipient) {
        return messageRepository.findByRecipientOrderBySentAtDesc(recipient);
    }

    public List<Message> getUnreadMessages(User recipient) {
        return messageRepository.findByRecipientAndIsReadFalse(recipient);
    }

    public List<Message> getConversation(User user1, User user2) {
        return messageRepository.findBySenderOrRecipientOrderBySentAtDesc(user1, user2);
    }

    public Message markAsRead(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        message.setIsRead(true);
        message.setReadAt(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}