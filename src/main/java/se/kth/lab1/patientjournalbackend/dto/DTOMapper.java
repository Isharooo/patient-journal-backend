package se.kth.lab1.patientjournalbackend.dto;

import se.kth.lab1.patientjournalbackend.model.*;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

    public UserDTO toUserDTO(User user) {
        if (user == null) return null;
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }

    public PatientDTO toPatientDTO(Patient patient) {
        if (patient == null) return null;
        return new PatientDTO(
                patient.getId(),
                toUserDTO(patient.getUser()),
                patient.getPersonalNumber(),
                patient.getDateOfBirth(),
                patient.getAddress(),
                patient.getPhoneNumber()
        );
    }

    public PractitionerDTO toPractitionerDTO(Practitioner practitioner) {
        if (practitioner == null) return null;
        return new PractitionerDTO(
                practitioner.getId(),
                toUserDTO(practitioner.getUser()),
                practitioner.getSpecialization(),
                practitioner.getOrganization() != null ? practitioner.getOrganization().getId() : null,
                practitioner.getOrganization() != null ? practitioner.getOrganization().getName() : null
        );
    }

    public EncounterDTO toEncounterDTO(Encounter encounter) {
        if (encounter == null) return null;
        return new EncounterDTO(
                encounter.getId(),
                encounter.getPatient().getId(),
                encounter.getPatient().getUser().getFirstName() + " " + encounter.getPatient().getUser().getLastName(),
                encounter.getPractitioner().getId(),
                encounter.getPractitioner().getUser().getFirstName() + " " + encounter.getPractitioner().getUser().getLastName(),
                encounter.getLocation() != null ? encounter.getLocation().getId() : null,
                encounter.getLocation() != null ? encounter.getLocation().getName() : null,
                encounter.getEncounterDate(),
                encounter.getNotes()
        );
    }

    public ObservationDTO toObservationDTO(Observation observation) {
        if (observation == null) return null;
        return new ObservationDTO(
                observation.getId(),
                observation.getPatient().getId(),
                observation.getPatient().getUser().getFirstName() + " " + observation.getPatient().getUser().getLastName(),
                observation.getEncounter() != null ? observation.getEncounter().getId() : null,
                observation.getObservationDate(),
                observation.getObservationType(),
                observation.getValue(),
                observation.getNotes()
        );
    }

    public ConditionDTO toConditionDTO(Condition condition) {
        if (condition == null) return null;
        return new ConditionDTO(
                condition.getId(),
                condition.getPatient().getId(),
                condition.getPatient().getUser().getFirstName() + " " + condition.getPatient().getUser().getLastName(),
                condition.getEncounter() != null ? condition.getEncounter().getId() : null,
                condition.getDiagnosedBy().getId(),
                condition.getDiagnosedBy().getUser().getFirstName() + " " + condition.getDiagnosedBy().getUser().getLastName(),
                condition.getDiagnosisCode(),
                condition.getDiagnosisName(),
                condition.getNotes(),
                condition.getDiagnosedDate()
        );
    }

    public MessageDTO toMessageDTO(Message message) {
        if (message == null) return null;
        return new MessageDTO(
                message.getId(),
                message.getSender().getId(),
                message.getSender().getFirstName() + " " + message.getSender().getLastName(),
                message.getRecipient().getId(),
                message.getRecipient().getFirstName() + " " + message.getRecipient().getLastName(),
                message.getSubject(),
                message.getContent(),
                message.getSentAt(),
                message.getReadAt(),
                message.getIsRead()
        );
    }
}