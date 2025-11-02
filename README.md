# Patient Journal System - Backend

Ett journalsystem för hantering av patientinformation byggt med Spring Boot.

## Teknologier

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring Security**
- **MySQL 8.0**
- **Docker**
- **Maven**

## Systemarkitektur

Systemet följer en tre-lagers arkitektur:

- **Controller Layer** - REST API endpoints
- **Service Layer** - Affärslogik
- **Repository Layer** - Databasåtkomst

## Funktioner

### Användarhantering
- Registrering av nya användare
- Inloggning
- Tre användarroller: PATIENT, DOCTOR, STAFF

### Patienthantering
- Skapa och uppdatera patientprofiler
- Personnummer, födelsedatum, kontaktinformation

### Vårdtillfällen (Encounters)
- Dokumentera vårdtillfällen
- Koppla till patient och läkare
- Anteckningar och plats

### Observationer
- Registrera mätningar (blodtryck, puls, vikt, etc.)
- Koppla till vårdtillfällen och patienter

### Diagnoser (Conditions)
- Fastställa diagnoser
- ICD-10 koder
- Koppla till vårdtillfällen och läkare

### Meddelandesystem
- Patienter kan skicka meddelanden till läkare/personal
- Läkare/personal kan svara på meddelanden
- Markera meddelanden som lästa

## Förutsättningar

- Docker Desktop installerat
- Java 17 eller senare
- Maven 3.6+
- Git

## Installation och körning

### 1. Klona projektet
```bash
git clone <your-repo-url>
cd patient-journal-backend
```

### 2. Bygg projektet
```bash
./mvnw clean package -DskipTests
```

**Windows:**
```bash
mvnw.cmd clean package -DskipTests
```

### 3. Bygg Docker-imagen
```bash
docker build -t patient-backend:latest .
```

### 4. Skapa Docker-nätverk
```bash
docker network create patient-network
```

### 5. Starta MySQL-container
```bash
docker run -d \
  --name mysql-db \
  --network patient-network \
  -e MYSQL_ROOT_PASSWORD=rootpassword \
  -e MYSQL_DATABASE=patient_journal \
  -p 3306:3306 \
  mysql:8.0
```

**Windows (Command Prompt):**
```batch
docker run -d --name mysql-db --network patient-network -e MYSQL_ROOT_PASSWORD=rootpassword -e MYSQL_DATABASE=patient_journal -p 3306:3306 mysql:8.0
```

Vänta ca 15-20 sekunder så MySQL hinner starta.

### 6. Starta Backend-container
```bash
docker run -d \
  --name patient-backend \
  --network patient-network \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql-db:3306/patient_journal?createDatabaseIfNotExist=true \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=rootpassword \
  -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
  -p 8080:8080 \
  patient-backend:latest
```

**Windows:**
```batch
docker run -d --name patient-backend --network patient-network -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql-db:3306/patient_journal?createDatabaseIfNotExist=true -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD=rootpassword -e SPRING_JPA_HIBERNATE_DDL_AUTO=update -p 8080:8080 patient-backend:latest
```

### 7. Verifiera att allt körs
```bash
docker ps
```

Du ska se två containers: `mysql-db` och `patient-backend`

**Kolla backend-loggar:**
```bash
docker logs patient-backend
```

Leta efter: `Started PatientJournalBackendApplication`

## API Endpoints

Backend körs på `http://localhost:8080`

### Autentisering

- `POST /api/auth/register` - Registrera ny användare
- `POST /api/auth/login` - Logga in

### Användare

- `GET /api/users` - Hämta alla användare
- `GET /api/users/{id}` - Hämta specifik användare
- `GET /api/users/role/{role}` - Hämta användare per roll
- `PUT /api/users/{id}` - Uppdatera användare
- `DELETE /api/users/{id}` - Ta bort användare

### Patienter

- `POST /api/patients` - Skapa patient
- `GET /api/patients` - Hämta alla patienter
- `GET /api/patients/{id}` - Hämta specifik patient
- `GET /api/patients/personal-number/{personalNumber}` - Hämta via personnummer
- `PUT /api/patients/{id}` - Uppdatera patient
- `DELETE /api/patients/{id}` - Ta bort patient

### Läkare/Personal

- `POST /api/practitioners` - Skapa practitioner
- `GET /api/practitioners` - Hämta alla practitioners
- `GET /api/practitioners/{id}` - Hämta specifik practitioner
- `PUT /api/practitioners/{id}` - Uppdatera practitioner
- `DELETE /api/practitioners/{id}` - Ta bort practitioner

### Vårdtillfällen

- `POST /api/encounters` - Skapa encounter
- `GET /api/encounters` - Hämta alla encounters
- `GET /api/encounters/{id}` - Hämta specifik encounter
- `GET /api/encounters/patient/{patientId}` - Hämta encounters för patient
- `PUT /api/encounters/{id}` - Uppdatera encounter
- `DELETE /api/encounters/{id}` - Ta bort encounter

### Observationer

- `POST /api/observations` - Skapa observation
- `GET /api/observations` - Hämta alla observationer
- `GET /api/observations/{id}` - Hämta specifik observation
- `GET /api/observations/patient/{patientId}` - Hämta observationer för patient
- `PUT /api/observations/{id}` - Uppdatera observation
- `DELETE /api/observations/{id}` - Ta bort observation

### Diagnoser

- `POST /api/conditions` - Skapa condition
- `GET /api/conditions` - Hämta alla conditions
- `GET /api/conditions/{id}` - Hämta specifik condition
- `GET /api/conditions/patient/{patientId}` - Hämta conditions för patient
- `PUT /api/conditions/{id}` - Uppdatera condition
- `DELETE /api/conditions/{id}` - Ta bort condition

### Meddelanden

- `POST /api/messages` - Skicka meddelande
- `GET /api/messages/{id}` - Hämta specifikt meddelande
- `GET /api/messages/sent/{userId}` - Hämta skickade meddelanden
- `GET /api/messages/received/{userId}` - Hämta mottagna meddelanden
- `GET /api/messages/unread/{userId}` - Hämta olästa meddelanden
- `PUT /api/messages/{id}/read` - Markera som läst
- `DELETE /api/messages/{id}` - Ta bort meddelande

### Organisationer

- `POST /api/organizations` - Skapa organisation
- `GET /api/organizations` - Hämta alla organisationer
- `GET /api/organizations/{id}` - Hämta specifik organisation

### Platser

- `POST /api/locations` - Skapa plats
- `GET /api/locations` - Hämta alla platser
- `GET /api/locations/{id}` - Hämta specifik plats

## Testning

### Med Postman

1. Importera eller skapa requests för endpoints ovan
2. Testa registrering: `POST http://localhost:8080/api/auth/register`
3. Testa inloggning: `POST http://localhost:8080/api/auth/login`
4. Testa övriga endpoints

### Exempel: Registrera läkare
```json
POST http://localhost:8080/api/auth/register

{
  "username": "doctor1",
  "password": "password123",
  "role": "DOCTOR",
  "firstName": "Anna",
  "lastName": "Andersson",
  "email": "anna.andersson@hospital.se"
}
```

## Stoppa och rensa containers
```bash
# Stoppa containers
docker stop patient-backend mysql-db

# Ta bort containers
docker rm patient-backend mysql-db

# Ta bort nätverk
docker network rm patient-network

# Ta bort image (om du vill bygga om)
docker rmi patient-backend:latest
```

## Lokal utveckling (utan Docker)

För att köra backend lokalt under utveckling:

1. Starta MySQL i Docker (steg 5 ovan)
2. Kör backend från IntelliJ eller via:
```bash
./mvnw spring-boot:run
```

Backend kommer då använda `application.properties` som pekar på `localhost:3306`.

## Databasschema

Systemet skapar automatiskt följande tabeller:

- `users` - Användare
- `patients` - Patientprofiler
- `practitioners` - Läkare/personal
- `organizations` - Organisationer
- `locations` - Platser
- `encounters` - Vårdtillfällen
- `observations` - Observationer/mätningar
- `conditions` - Diagnoser
- `messages` - Meddelanden

## Säkerhet

- Lösenord krypteras med BCrypt
- CORS konfigurerat för `http://localhost:3000` (frontend)
- Spring Security konfigurerat

**OBS:** Detta är ett utvecklingsprojekt. För produktion behövs:
- JWT-tokens för autentisering
- HTTPS
- Bättre lösenordspolicy
- Rate limiting
- Säkra environment variables

## Projektstruktur
```
src/
├── main/
│   ├── java/se/kth/lab1/patientjournalbackend/
│   │   ├── config/          # Konfiguration (Security, CORS)
│   │   ├── controller/      # REST Controllers
│   │   ├── service/         # Business Logic
│   │   ├── repository/      # Data Access
│   │   ├── model/           # Entities
│   │   └── PatientJournalBackendApplication.java
│   └── resources/
│       └── application.properties
├── Dockerfile
└── pom.xml
```

## Författare

[Ditt namn]

## Licens

[Din licens]