package se.kth.lab1.patientjournalbackend.controller;

import se.kth.lab1.patientjournalbackend.dto.DTOMapper;
import se.kth.lab1.patientjournalbackend.dto.MessageDTO;
import se.kth.lab1.patientjournalbackend.model.Message;
import se.kth.lab1.patientjournalbackend.service.MessageService;
import se.kth.lab1.patientjournalbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private DTOMapper dtoMapper;

    @PostMapping
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody Message message) {
        Message sent = messageService.sendMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.toMessageDTO(sent));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMessageById(@PathVariable Long id) {
        return messageService.getMessageById(id)
                .map(message -> ResponseEntity.ok(dtoMapper.toMessageDTO(message)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sent/{userId}")
    public ResponseEntity<?> getSentMessages(@PathVariable Long userId) {
        return userService.getUserById(userId)
                .map(user -> {
                    List<MessageDTO> messages = messageService.getMessagesBySender(user).stream()
                            .map(dtoMapper::toMessageDTO)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(messages);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/received/{userId}")
    public ResponseEntity<?> getReceivedMessages(@PathVariable Long userId) {
        return userService.getUserById(userId)
                .map(user -> {
                    List<MessageDTO> messages = messageService.getMessagesByRecipient(user).stream()
                            .map(dtoMapper::toMessageDTO)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(messages);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/unread/{userId}")
    public ResponseEntity<?> getUnreadMessages(@PathVariable Long userId) {
        return userService.getUserById(userId)
                .map(user -> {
                    List<MessageDTO> messages = messageService.getUnreadMessages(user).stream()
                            .map(dtoMapper::toMessageDTO)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(messages);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        try {
            Message marked = messageService.markAsRead(id);
            return ResponseEntity.ok(dtoMapper.toMessageDTO(marked));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }
}