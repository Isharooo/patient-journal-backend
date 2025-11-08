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
        // 15 s connect/read timeout
        context.getRestfulClientFactory().setConnectTimeout(15_000);
        context.getRestfulClientFactory().setSocketTimeout(15_000);
        context.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER); // hoppa cert-validering mot /metadata vid start
        this.client = context.newRestfulGenericClient(BASE_URL);
        this.client.registerInterceptor(new ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor("User-Agent","patient-journal-backend/1.0"));
    }

    /**
     * Hämtar alla patienter från HAPI servern med pagination support
     */
    public List<Patient> getAllPatients() {
        List<Patient> allPatients = new ArrayList<>();

        // Initial search
        Bundle bundle = client
                .search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .execute();

        // Add patients from first page
        allPatients.addAll(extractPatientsFromBundle(bundle));

        // Handle pagination - fetch all pages
        while (bundle.getLink(Bundle.LINK_NEXT) != null) {
            bundle = client
                    .loadPage()
                    .next(bundle)
                    .execute();
            allPatients.addAll(extractPatientsFromBundle(bundle));
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
                .withId(id)
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

        List<Patient> patients = extractPatientsFromBundle(bundle);
        return patients.isEmpty() ? null : patients.get(0);
    }

    /**
     * Hämtar alla observationer från HAPI
     */
    public List<Observation> getAllObservations() {
        List<Observation> allObservations = new ArrayList<>();

        Bundle bundle = client
                .search()
                .forResource(Observation.class)
                .returnBundle(Bundle.class)
                .execute();

        allObservations.addAll(extractObservationsFromBundle(bundle));

        while (bundle.getLink(Bundle.LINK_NEXT) != null) {
            bundle = client
                    .loadPage()
                    .next(bundle)
                    .execute();
            allObservations.addAll(extractObservationsFromBundle(bundle));
        }

        return allObservations;
    }

    /**
     * Hämtar observationer för en specifik patient
     */
    public List<Observation> getObservationsByPatientId(String patientId) {
        List<Observation> allObservations = new ArrayList<>();

        Bundle bundle = client
                .search()
                .forResource(Observation.class)
                .where(Observation.SUBJECT.hasId(patientId))
                .returnBundle(Bundle.class)
                .execute();

        allObservations.addAll(extractObservationsFromBundle(bundle));

        while (bundle.getLink(Bundle.LINK_NEXT) != null) {
            bundle = client
                    .loadPage()
                    .next(bundle)
                    .execute();
            allObservations.addAll(extractObservationsFromBundle(bundle));
        }

        return allObservations;
    }

    /**
     * Hämtar alla encounters
     */
    public List<Encounter> getAllEncounters() {
        List<Encounter> allEncounters = new ArrayList<>();

        Bundle bundle = client
                .search()
                .forResource(Encounter.class)
                .returnBundle(Bundle.class)
                .execute();

        allEncounters.addAll(extractEncountersFromBundle(bundle));

        while (bundle.getLink(Bundle.LINK_NEXT) != null) {
            bundle = client
                    .loadPage()
                    .next(bundle)
                    .execute();
            allEncounters.addAll(extractEncountersFromBundle(bundle));
        }

        return allEncounters;
    }

    /**
     * Hämtar encounters för en specifik patient
     */
    public List<Encounter> getEncountersByPatientId(String patientId) {
        List<Encounter> allEncounters = new ArrayList<>();

        Bundle bundle = client
                .search()
                .forResource(Encounter.class)
                .where(Encounter.SUBJECT.hasId(patientId))
                .returnBundle(Bundle.class)
                .execute();

        allEncounters.addAll(extractEncountersFromBundle(bundle));

        while (bundle.getLink(Bundle.LINK_NEXT) != null) {
            bundle = client
                    .loadPage()
                    .next(bundle)
                    .execute();
            allEncounters.addAll(extractEncountersFromBundle(bundle));
        }

        return allEncounters;
    }

    /**
     * Hämtar alla conditions
     */
    public List<Condition> getAllConditions() {
        List<Condition> allConditions = new ArrayList<>();

        Bundle bundle = client
                .search()
                .forResource(Condition.class)
                .returnBundle(Bundle.class)
                .execute();

        allConditions.addAll(extractConditionsFromBundle(bundle));

        while (bundle.getLink(Bundle.LINK_NEXT) != null) {
            bundle = client
                    .loadPage()
                    .next(bundle)
                    .execute();
            allConditions.addAll(extractConditionsFromBundle(bundle));
        }

        return allConditions;
    }

    /**
     * Hämtar conditions för en specifik patient
     */
    public List<Condition> getConditionsByPatientId(String patientId) {
        List<Condition> allConditions = new ArrayList<>();

        Bundle bundle = client
                .search()
                .forResource(Condition.class)
                .where(Condition.SUBJECT.hasId(patientId))
                .returnBundle(Bundle.class)
                .execute();

        allConditions.addAll(extractConditionsFromBundle(bundle));

        while (bundle.getLink(Bundle.LINK_NEXT) != null) {
            bundle = client
                    .loadPage()
                    .next(bundle)
                    .execute();
            allConditions.addAll(extractConditionsFromBundle(bundle));
        }

        return allConditions;
    }

    /**
     * Hämtar alla practitioners
     */
    public List<Practitioner> getAllPractitioners() {
        List<Practitioner> allPractitioners = new ArrayList<>();

        Bundle bundle = client
                .search()
                .forResource(Practitioner.class)
                .returnBundle(Bundle.class)
                .execute();

        allPractitioners.addAll(extractPractitionersFromBundle(bundle));

        while (bundle.getLink(Bundle.LINK_NEXT) != null) {
            bundle = client
                    .loadPage()
                    .next(bundle)
                    .execute();
            allPractitioners.addAll(extractPractitionersFromBundle(bundle));
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

    // Helper methods för att extrahera resurser från Bundles

    private List<Patient> extractPatientsFromBundle(Bundle bundle) {
        return BundleUtil.toListOfEntries(context, bundle).stream()
                .map(entry -> (Patient) entry.getResource())
                .collect(Collectors.toList());
    }

    private List<Observation> extractObservationsFromBundle(Bundle bundle) {
        return BundleUtil.toListOfEntries(context, bundle).stream()
                .map(entry -> (Observation) entry.getResource())
                .collect(Collectors.toList());
    }

    private List<Encounter> extractEncountersFromBundle(Bundle bundle) {
        return BundleUtil.toListOfEntries(context, bundle).stream()
                .map(entry -> (Encounter) entry.getResource())
                .collect(Collectors.toList());
    }

    private List<Condition> extractConditionsFromBundle(Bundle bundle) {
        return BundleUtil.toListOfEntries(context, bundle).stream()
                .map(entry -> (Condition) entry.getResource())
                .collect(Collectors.toList());
    }

    private List<Practitioner> extractPractitionersFromBundle(Bundle bundle) {
        return BundleUtil.toListOfEntries(context, bundle).stream()
                .map(entry -> (Practitioner) entry.getResource())
                .collect(Collectors.toList());
    }
}