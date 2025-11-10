package se.kth.lab1.patientjournalbackend.dto;

import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;
import se.kth.lab1.patientjournalbackend.dto.*;

import java.time.ZoneId;

@Component
public class HapiFhirMapper {

    private static final String PNR_SYSTEM = "http://electronichealth.se/identifier/personnummer";

    public PatientDTO toPatientDTO(Patient fhirPatient) {
        if (fhirPatient == null) return null;

        PatientDTO dto = new PatientDTO();

        String fhirId = fhirPatient.getIdElement().getIdPart();
        dto.setFhirId(fhirId);

        dto.setId((long) Math.abs(fhirId.hashCode()));

        if (!fhirPatient.getName().isEmpty()) {
            HumanName name = fhirPatient.getName().get(0);
            UserDTO userDTO = new UserDTO();
            userDTO.setId(dto.getId());
            userDTO.setFirstName(name.getGivenAsSingleString());
            userDTO.setLastName(name.getFamily());
            dto.setUser(userDTO);
        }


        for (Identifier identifier : fhirPatient.getIdentifier()) {
            if (PNR_SYSTEM.equals(identifier.getSystem()) && identifier.hasValue()) {
                dto.setPersonalNumber(identifier.getValue());
                break;
            }
        }

        if (dto.getPersonalNumber() == null) {
            for (Identifier identifier : fhirPatient.getIdentifier()) {
                if (identifier.hasValue()) {
                    dto.setPersonalNumber(identifier.getValue());
                    break;
                }
            }
        }

        if (fhirPatient.hasBirthDate()) {
            dto.setDateOfBirth(fhirPatient.getBirthDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }

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

        for (ContactPoint telecom : fhirPatient.getTelecom()) {
            if (telecom.getSystem() == ContactPoint.ContactPointSystem.PHONE) {
                dto.setPhoneNumber(telecom.getValue());
                break;
            }
        }

        return dto;
    }

    public ObservationDTO toObservationDTO(Observation fhirObservation) {
        if (fhirObservation == null) return null;

        ObservationDTO dto = new ObservationDTO();

        String fhirId = fhirObservation.getIdElement().getIdPart();
        dto.setId((long) Math.abs(fhirId.hashCode()));

        if (fhirObservation.hasSubject()) {
            String patientId = fhirObservation.getSubject().getReferenceElement().getIdPart();
            dto.setPatientId((long) Math.abs(patientId.hashCode()));
        }

        if (fhirObservation.hasEffectiveDateTimeType()) {
            dto.setObservationDate(fhirObservation.getEffectiveDateTimeType()
                    .getValue().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
        }

        if (fhirObservation.hasCode() && !fhirObservation.getCode().getCoding().isEmpty()) {
            dto.setObservationType(fhirObservation.getCode().getCoding().get(0).getDisplay());
        }

        if (fhirObservation.hasValueQuantity()) {
            Quantity quantity = fhirObservation.getValueQuantity();
            dto.setValue(quantity.getValue() + " " + quantity.getUnit());
        } else if (fhirObservation.hasValueStringType()) {
            dto.setValue(fhirObservation.getValueStringType().getValue());
        }

        if (!fhirObservation.getNote().isEmpty()) {
            dto.setNotes(fhirObservation.getNote().get(0).getText());
        }

        return dto;
    }

    public EncounterDTO toEncounterDTO(Encounter fhirEncounter) {
        if (fhirEncounter == null) return null;

        EncounterDTO dto = new EncounterDTO();

        String fhirId = fhirEncounter.getIdElement().getIdPart();
        dto.setId((long) Math.abs(fhirId.hashCode()));

        if (fhirEncounter.hasSubject()) {
            String patientId = fhirEncounter.getSubject().getReferenceElement().getIdPart();
            dto.setPatientId((long) Math.abs(patientId.hashCode()));
        }

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

        if (fhirEncounter.hasPeriod() && fhirEncounter.getPeriod().hasStart()) {
            dto.setEncounterDate(fhirEncounter.getPeriod().getStart().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
        }

        if (fhirEncounter.hasText() && fhirEncounter.getText().hasDiv()) {
            String notes = fhirEncounter.getText().getDivAsString()
                    .replaceAll("<[^>]*>", "")
                    .trim();
            dto.setNotes(notes);
        }

        return dto;
    }

    public ConditionDTO toConditionDTO(Condition fhirCondition) {
        if (fhirCondition == null) return null;

        ConditionDTO dto = new ConditionDTO();

        String fhirId = fhirCondition.getIdElement().getIdPart();
        dto.setId((long) Math.abs(fhirId.hashCode()));

        if (fhirCondition.hasSubject()) {
            String patientId = fhirCondition.getSubject().getReferenceElement().getIdPart();
            dto.setPatientId((long) Math.abs(patientId.hashCode()));
        }

        if (fhirCondition.hasEncounter()) {
            String encId = fhirCondition.getEncounter().getReferenceElement().getIdPart();
            dto.setEncounterId((long) Math.abs(encId.hashCode()));
        }

        if (fhirCondition.hasRecorder()) {
            Reference ref = fhirCondition.getRecorder();
            String practId = ref.getReferenceElement().getIdPart();
            dto.setDiagnosedById((long) Math.abs(practId.hashCode()));
            if (ref.hasDisplay()) {
                dto.setDiagnosedByName(ref.getDisplay());
            }
        }

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

        if (!fhirCondition.getNote().isEmpty()) {
            dto.setNotes(fhirCondition.getNote().get(0).getText());
        }

        if (fhirCondition.hasRecordedDate()) {
            dto.setDiagnosedDate(fhirCondition.getRecordedDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
        }

        return dto;
    }

    public PractitionerDTO toPractitionerDTO(Practitioner fhirPractitioner) {
        if (fhirPractitioner == null) return null;

        PractitionerDTO dto = new PractitionerDTO();

        String fhirId = fhirPractitioner.getIdElement().getIdPart();
        dto.setId((long) Math.abs(fhirId.hashCode()));

        if (!fhirPractitioner.getName().isEmpty()) {
            HumanName name = fhirPractitioner.getName().get(0);
            UserDTO userDTO = new UserDTO();
            userDTO.setId(dto.getId());
            userDTO.setFirstName(name.getGivenAsSingleString());
            userDTO.setLastName(name.getFamily());
            dto.setUser(userDTO);
        }

        if (!fhirPractitioner.getQualification().isEmpty()) {
            Practitioner.PractitionerQualificationComponent qualification = fhirPractitioner.getQualification().get(0);
            if (qualification.hasCode()) {
                dto.setSpecialization(qualification.getCode().getText());
            }
        }

        return dto;
    }
}
















