package se.kth.lab1.patientjournalbackend.dto;

import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;
import se.kth.lab1.patientjournalbackend.dto.*;

import java.time.ZoneId;

@Component
public class HapiFhirMapper {

    /**
     * Konverterar FHIR Patient till PatientDTO
     * VIKTIGT: FHIR använder UUID strings som ID, inte Long!
     */
    public PatientDTO toPatientDTO(Patient fhirPatient) {
        if (fhirPatient == null) return null;

        PatientDTO dto = new PatientDTO();

        // FHIR ID är en UUID string, vi kan inte konvertera till Long
        // Istället använder vi hashCode eller låter ID vara null
        // Frontend behöver uppdateras för att hantera FHIR IDs som strings
        String fhirId = fhirPatient.getIdElement().getIdPart();

        // Temporär lösning: använd hashCode för att skapa ett Long ID
        // Detta är inte perfekt men tillåter oss att använda befintlig DTO struktur
        dto.setId((long) Math.abs(fhirId.hashCode()));

        // Extract name
        if (!fhirPatient.getName().isEmpty()) {
            HumanName name = fhirPatient.getName().get(0);
            UserDTO userDTO = new UserDTO();
            userDTO.setId(dto.getId()); // Använd samma ID
            userDTO.setFirstName(name.getGivenAsSingleString());
            userDTO.setLastName(name.getFamily());
            dto.setUser(userDTO);
        }

        // Extract personal number from identifiers
        for (Identifier identifier : fhirPatient.getIdentifier()) {
            if (identifier.getSystem() != null &&
                    (identifier.getSystem().contains("personnummer") ||
                            identifier.getSystem().contains("ssn") ||
                            identifier.getSystem().contains("personal"))) {
                dto.setPersonalNumber(identifier.getValue());
                break;
            }
        }

        // Extract birth date
        if (fhirPatient.hasBirthDate()) {
            dto.setDateOfBirth(fhirPatient.getBirthDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }

        // Extract address
        if (!fhirPatient.getAddress().isEmpty()) {
            Address address = fhirPatient.getAddress().get(0);
            StringBuilder addressStr = new StringBuilder();
            for (var line : address.getLine()) {
                if (addressStr.length() > 0) addressStr.append(" ");
                addressStr.append(line.getValue());
            }
            if (address.hasCity()) {
                if (addressStr.length() > 0) addressStr.append(", ");
                addressStr.append(address.getCity());
            }
            dto.setAddress(addressStr.toString());
        }

        // Extract phone
        if (!fhirPatient.getTelecom().isEmpty()) {
            for (ContactPoint telecom : fhirPatient.getTelecom()) {
                if (telecom.getSystem() == ContactPoint.ContactPointSystem.PHONE) {
                    dto.setPhoneNumber(telecom.getValue());
                    break;
                }
            }
        }

        return dto;
    }

    /**
     * Konverterar FHIR Observation till ObservationDTO
     */
    public ObservationDTO toObservationDTO(Observation fhirObservation) {
        if (fhirObservation == null) return null;

        ObservationDTO dto = new ObservationDTO();

        // Använd hashCode för UUID -> Long konvertering
        String fhirId = fhirObservation.getIdElement().getIdPart();
        dto.setId((long) Math.abs(fhirId.hashCode()));

        // Extract patient ID
        if (fhirObservation.hasSubject()) {
            String patientId = fhirObservation.getSubject().getReferenceElement().getIdPart();
            dto.setPatientId((long) Math.abs(patientId.hashCode()));
        }

        // Extract observation date
        if (fhirObservation.hasEffectiveDateTimeType()) {
            dto.setObservationDate(fhirObservation.getEffectiveDateTimeType()
                    .getValue().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
        }

        // Extract observation type
        if (fhirObservation.hasCode() && !fhirObservation.getCode().getCoding().isEmpty()) {
            dto.setObservationType(fhirObservation.getCode().getCoding().get(0).getDisplay());
        }

        // Extract value
        if (fhirObservation.hasValueQuantity()) {
            Quantity quantity = fhirObservation.getValueQuantity();
            dto.setValue(quantity.getValue() + " " + quantity.getUnit());
        } else if (fhirObservation.hasValueStringType()) {
            dto.setValue(fhirObservation.getValueStringType().getValue());
        }

        // Extract notes
        if (!fhirObservation.getNote().isEmpty()) {
            dto.setNotes(fhirObservation.getNote().get(0).getText());
        }

        return dto;
    }

    /**
     * Konverterar FHIR Encounter till EncounterDTO
     */
    public EncounterDTO toEncounterDTO(Encounter fhirEncounter) {
        if (fhirEncounter == null) return null;

        EncounterDTO dto = new EncounterDTO();

        // Använd hashCode för UUID -> Long konvertering
        String fhirId = fhirEncounter.getIdElement().getIdPart();
        dto.setId((long) Math.abs(fhirId.hashCode()));

        // Extract patient ID
        if (fhirEncounter.hasSubject()) {
            String patientId = fhirEncounter.getSubject().getReferenceElement().getIdPart();
            dto.setPatientId((long) Math.abs(patientId.hashCode()));
        }

        // Extract practitioner ID and name
        if (!fhirEncounter.getParticipant().isEmpty()) {
            for (Encounter.EncounterParticipantComponent participant : fhirEncounter.getParticipant()) {
                if (participant.hasIndividual()) {
                    Reference ref = participant.getIndividual();
                    String practId = ref.getReferenceElement().getIdPart();
                    dto.setPractitionerId((long) Math.abs(practId.hashCode()));
                    if (ref.hasDisplay()) {
                        dto.setPractitionerName(ref.getDisplay());
                    }
                    break;
                }
            }
        }

        // Extract location
        if (!fhirEncounter.getLocation().isEmpty()) {
            Encounter.EncounterLocationComponent location = fhirEncounter.getLocation().get(0);
            if (location.hasLocation()) {
                Reference ref = location.getLocation();
                String locId = ref.getReferenceElement().getIdPart();
                dto.setLocationId((long) Math.abs(locId.hashCode()));
                if (ref.hasDisplay()) {
                    dto.setLocationName(ref.getDisplay());
                }
            }
        }

        // Extract encounter date
        if (fhirEncounter.hasPeriod() && fhirEncounter.getPeriod().hasStart()) {
            dto.setEncounterDate(fhirEncounter.getPeriod().getStart().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
        }

        // Extract notes
        if (fhirEncounter.hasText() && fhirEncounter.getText().hasDiv()) {
            // Ta bort HTML tags från div
            String notes = fhirEncounter.getText().getDivAsString()
                    .replaceAll("<[^>]*>", "")
                    .trim();
            dto.setNotes(notes);
        }

        return dto;
    }

    /**
     * Konverterar FHIR Condition till ConditionDTO
     */
    public ConditionDTO toConditionDTO(Condition fhirCondition) {
        if (fhirCondition == null) return null;

        ConditionDTO dto = new ConditionDTO();

        // Använd hashCode för UUID -> Long konvertering
        String fhirId = fhirCondition.getIdElement().getIdPart();
        dto.setId((long) Math.abs(fhirId.hashCode()));

        // Extract patient ID
        if (fhirCondition.hasSubject()) {
            String patientId = fhirCondition.getSubject().getReferenceElement().getIdPart();
            dto.setPatientId((long) Math.abs(patientId.hashCode()));
        }

        // Extract encounter ID
        if (fhirCondition.hasEncounter()) {
            String encId = fhirCondition.getEncounter().getReferenceElement().getIdPart();
            dto.setEncounterId((long) Math.abs(encId.hashCode()));
        }

        // Extract recorder (diagnosed by)
        if (fhirCondition.hasRecorder()) {
            Reference ref = fhirCondition.getRecorder();
            String practId = ref.getReferenceElement().getIdPart();
            dto.setDiagnosedById((long) Math.abs(practId.hashCode()));
            if (ref.hasDisplay()) {
                dto.setDiagnosedByName(ref.getDisplay());
            }
        }

        // Extract diagnosis code and name
        if (fhirCondition.hasCode()) {
            CodeableConcept code = fhirCondition.getCode();
            if (!code.getCoding().isEmpty()) {
                Coding coding = code.getCoding().get(0);
                dto.setDiagnosisCode(coding.getCode());
                dto.setDiagnosisName(coding.getDisplay() != null ? coding.getDisplay() : code.getText());
            } else if (code.hasText()) {
                dto.setDiagnosisName(code.getText());
            }
        }

        // Extract notes
        if (!fhirCondition.getNote().isEmpty()) {
            dto.setNotes(fhirCondition.getNote().get(0).getText());
        }

        // Extract diagnosed date
        if (fhirCondition.hasRecordedDate()) {
            dto.setDiagnosedDate(fhirCondition.getRecordedDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
        }

        return dto;
    }

    /**
     * Konverterar FHIR Practitioner till PractitionerDTO
     */
    public PractitionerDTO toPractitionerDTO(Practitioner fhirPractitioner) {
        if (fhirPractitioner == null) return null;

        PractitionerDTO dto = new PractitionerDTO();

        // Använd hashCode för UUID -> Long konvertering
        String fhirId = fhirPractitioner.getIdElement().getIdPart();
        dto.setId((long) Math.abs(fhirId.hashCode()));

        // Extract name
        if (!fhirPractitioner.getName().isEmpty()) {
            HumanName name = fhirPractitioner.getName().get(0);
            UserDTO userDTO = new UserDTO();
            userDTO.setId(dto.getId());
            userDTO.setFirstName(name.getGivenAsSingleString());
            userDTO.setLastName(name.getFamily());
            dto.setUser(userDTO);
        }

        // Extract specialization/qualification
        if (!fhirPractitioner.getQualification().isEmpty()) {
            Practitioner.PractitionerQualificationComponent qualification = fhirPractitioner.getQualification().get(0);
            if (qualification.hasCode()) {
                dto.setSpecialization(qualification.getCode().getText());
            }
        }

        return dto;
    }
}