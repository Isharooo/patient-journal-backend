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
        context.getRestfulClientFactory().setConnectTimeout(60_000);
        context.getRestfulClientFactory().setSocketTimeout(60_000);
        context.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        this.client = context.newRestfulGenericClient(BASE_URL);
        this.client.registerInterceptor(new ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor("User-Agent","patient-journal-backend/1.0"));
    }

    public List<Patient> getAllPatients() {
        List<Patient> allPatients = new ArrayList<>();


        Bundle bundle = client
                .search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .execute();

        allPatients.addAll(
                BundleUtil.toListOfEntries(context, bundle).stream()
                        .map(entry -> (Patient) entry.getResource())
                        .collect(Collectors.toList())
        );

        while (bundle.getLink(Bundle.LINK_NEXT) != null) {
            bundle = client
                    .loadPage()
                    .next(bundle)
                    .execute();
            allPatients.addAll(
                    BundleUtil.toListOfEntries(context, bundle).stream()
                            .map(entry -> (Patient) entry.getResource())
                            .collect(Collectors.toList())
            );
        }

        return allPatients;
    }

    public Patient getPatientById(String id) {
        return client
                .read()
                .resource(Patient.class)
                .withId(id)
                .execute();
    }

    public Patient getPatientByPersonalNumber(String personalNumber) {
        Bundle bundle = client
                .search()
                .forResource(Patient.class)
                .where(Patient.IDENTIFIER.exactly().code(personalNumber))
                .returnBundle(Bundle.class)
                .execute();

        List<Patient> patients = BundleUtil.toListOfEntries(context, bundle).stream()
                .map(entry -> (Patient) entry.getResource())
                .collect(Collectors.toList());

        return patients.isEmpty() ? null : patients.get(0);
    }


    public List<Observation> getObservationsByPatientId(String patientId) {
        List<Observation> allObservations = new ArrayList<>();

        Bundle bundle = client
                .search()
                .forResource(Observation.class)
                .where(Observation.SUBJECT.hasId(patientId))
                .returnBundle(Bundle.class)
                .execute();

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
            allObservations.addAll(
                    BundleUtil.toListOfEntries(context, bundle).stream()
                            .map(entry -> (Observation) entry.getResource())
                            .collect(Collectors.toList())
            );
        }

        return allObservations;
    }

    public List<Encounter> getEncountersByPatientId(String patientId) {
        List<Encounter> allEncounters = new ArrayList<>();

        Bundle bundle = client
                .search()
                .forResource(Encounter.class)
                .where(Encounter.SUBJECT.hasId(patientId))
                .returnBundle(Bundle.class)
                .execute();

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
            allEncounters.addAll(
                    BundleUtil.toListOfEntries(context, bundle).stream()
                            .map(entry -> (Encounter) entry.getResource())
                            .collect(Collectors.toList())
            );
        }

        return allEncounters;
    }

    public List<Condition> getConditionsByPatientId(String patientId) {
        List<Condition> allConditions = new ArrayList<>();

        Bundle bundle = client
                .search()
                .forResource(Condition.class)
                .where(Condition.SUBJECT.hasId(patientId))
                .returnBundle(Bundle.class)
                .execute();

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
            allConditions.addAll(
                    BundleUtil.toListOfEntries(context, bundle).stream()
                            .map(entry -> (Condition) entry.getResource())
                            .collect(Collectors.toList())
            );
        }

        return allConditions;
    }

    public List<Practitioner> getAllPractitioners() {
        List<Practitioner> allPractitioners = new ArrayList<>();

        Bundle bundle = client
                .search()
                .forResource(Practitioner.class)
                .returnBundle(Bundle.class)
                .execute();

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
            allPractitioners.addAll(
                    BundleUtil.toListOfEntries(context, bundle).stream()
                            .map(entry -> (Practitioner) entry.getResource())
                            .collect(Collectors.toList())
            );
        }

        return allPractitioners;
    }

    public Practitioner getPractitionerById(String id) {
        return client
                .read()
                .resource(Practitioner.class)
                .withId(id)
                .execute();
    }
}