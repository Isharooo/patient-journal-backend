package se.kth.lab1.patientjournalbackend.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HapiFhirService {

    private final FhirContext context;
    private final IGenericClient client;
    private static final String BASE_URL = "https://hapi-fhir.app.cloud.cbh.kth.se/fhir";

    public HapiFhirService() {
        this.context = FhirContext.forR4();
        // 60 sekunders timeout, eftersom KTH-servern kan vara långsam
        context.getRestfulClientFactory().setConnectTimeout(60_000);
        context.getRestfulClientFactory().setSocketTimeout(60_000);
        context.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER); // hoppa cert-validering mot /metadata vid start
        this.client = context.newRestfulGenericClient(BASE_URL);
        this.client.registerInterceptor(new ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor("User-Agent","patient-journal-backend/1.0"));
    }

    /**
     * Hämtar ALLA patienter från HAPI servern med fullt stöd för pagination.
     */
    public List<Patient> getAllPatients() {
        List<Patient> allPatients = new ArrayList<>();

        // Initial search
        Bundle bundle = client
                .search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .execute();

        // Extrahera resurser från första sidan (FIX: Använder toListOfEntries)
        allPatients.addAll(
                BundleUtil.toListOfEntries(context, bundle).stream()
                        .map(entry -> (Patient) entry.getResource())
                        .collect(Collectors.toList())
        );

        // Hantera pagination - hämta alla återstående sidor
        while (bundle.getLink(Bundle.LINK_NEXT) != null) {
            bundle = client
                    .loadPage()
                    .next(bundle)
                    .execute();
            // Extrahera resurser från nästa sida (FIX: Använder toListOfEntries)
            allPatients.addAll(
                    BundleUtil.toListOfEntries(context, bundle).stream()
                            .map(entry -> (Patient) entry.getResource())
                            .collect(Collectors.toList())
            );
        }

        return allPatients;
    }

    /**
     * Hämtar en patient baserat på ID
     */
    public Patient getPatientById(String id) {
        return client
                .read()
                .resource(Patient.class)
                .withId(id) // Använder det rena sträng-ID:t
                .execute();
    }

    /**
     * Söker patient baserat på personnummer (identifier)
     */
    public Patient getPatientByPersonalNumber(String personalNumber) {
        Bundle bundle = client
                .search()
                .forResource(Patient.class)
                .where(Patient.IDENTIFIER.exactly().code(personalNumber))
                .returnBundle(Bundle.class)
                .execute();

        // Använder toListOfEntries här också för konsekvens
        List<Patient> patients = BundleUtil.toListOfEntries(context, bundle).stream()
                .map(entry -> (Patient) entry.getResource())
                .collect(Collectors.toList());

        return patients.isEmpty() ? null : patients.get(0);
    }

    /**
     * Hämtar observationer för en specifik patient, med stöd för pagination.
     */
    public List<Observation> getObservationsByPatientId(String patientId) {
        List<Observation> allObservations = new ArrayList<>();

        Bundle bundle = client
                .search()
                .forResource(Observation.class)
                .where(Observation.SUBJECT.hasId(patientId))
                .returnBundle(Bundle.class)
                .execute();

        // FIX: Använder toListOfEntries
        allObservations.addAll(
                BundleUtil.toListOfEntries(context, bundle).stream()
                        .map(entry -> (Observation) entry.getResource())
                        .collect(Collectors.toList())
        );

        while (bundle.getLink(Bundle.LINK_NEXT) != null) {
            bundle = client
                    .loadPage()
                    .next(bundle)
                    .execute();
            // FIX: Använder toListOfEntries
            allObservations.addAll(
                    BundleUtil.toListOfEntries(context, bundle).stream()
                            .map(entry -> (Observation) entry.getResource())
                            .collect(Collectors.toList())
            );
        }

        return allObservations;
    }

    /**
     * Hämtar encounters för en specifik patient, med stöd för pagination.
     */
    public List<Encounter> getEncountersByPatientId(String patientId) {
        List<Encounter> allEncounters = new ArrayList<>();

        Bundle bundle = client
                .search()
                .forResource(Encounter.class)
                .where(Encounter.SUBJECT.hasId(patientId))
                .returnBundle(Bundle.class)
                .execute();

        // FIX: Använder toListOfEntries
        allEncounters.addAll(
                BundleUtil.toListOfEntries(context, bundle).stream()
                        .map(entry -> (Encounter) entry.getResource())
                        .collect(Collectors.toList())
        );

        while (bundle.getLink(Bundle.LINK_NEXT) != null) {
            bundle = client
                    .loadPage()
                    .next(bundle)
                    .execute();
            // FIX: Använder toListOfEntries
            allEncounters.addAll(
                    BundleUtil.toListOfEntries(context, bundle).stream()
                            .map(entry -> (Encounter) entry.getResource())
                            .collect(Collectors.toList())
            );
        }

        return allEncounters;
    }

    /**
     * Hämtar conditions för en specifik patient, med stöd för pagination.
     */
    public List<Condition> getConditionsByPatientId(String patientId) {
        List<Condition> allConditions = new ArrayList<>();

        Bundle bundle = client
                .search()
                .forResource(Condition.class)
                .where(Condition.SUBJECT.hasId(patientId))
                .returnBundle(Bundle.class)
                .execute();

        // FIX: Använder toListOfEntries
        allConditions.addAll(
                BundleUtil.toListOfEntries(context, bundle).stream()
                        .map(entry -> (Condition) entry.getResource())
                        .collect(Collectors.toList())
        );

        while (bundle.getLink(Bundle.LINK_NEXT) != null) {
            bundle = client
                    .loadPage()
                    .next(bundle)
                    .execute();
            // FIX: Använder toListOfEntries
            allConditions.addAll(
                    BundleUtil.toListOfEntries(context, bundle).stream()
                            .map(entry -> (Condition) entry.getResource())
                            .collect(Collectors.toList())
            );
        }

        return allConditions;
    }

    /**
     * Hämtar alla practitioners, med stöd för pagination.
     */
    public List<Practitioner> getAllPractitioners() {
        List<Practitioner> allPractitioners = new ArrayList<>();

        Bundle bundle = client
                .search()
                .forResource(Practitioner.class)
                .returnBundle(Bundle.class)
                .execute();

        // FIX: Använder toListOfEntries
        allPractitioners.addAll(
                BundleUtil.toListOfEntries(context, bundle).stream()
                        .map(entry -> (Practitioner) entry.getResource())
                        .collect(Collectors.toList())
        );

        while (bundle.getLink(Bundle.LINK_NEXT) != null) {
            bundle = client
                    .loadPage()
                    .next(bundle)
                    .execute();
            // FIX: Använder toListOfEntries
            allPractitioners.addAll(
                    BundleUtil.toListOfEntries(context, bundle).stream()
                            .map(entry -> (Practitioner) entry.getResource())
                            .collect(Collectors.toList())
            );
        }

        return allPractitioners;
    }

    /**
     * Hämtar en practitioner baserat på ID
     */
    public Practitioner getPractitionerById(String id) {
        return client
                .read()
                .resource(Practitioner.class)
                .withId(id)
                .execute();
    }
}