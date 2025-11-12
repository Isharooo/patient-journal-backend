@echo off
ECHO 1/4: Stoppar och tar bort befintlig patient-backend
docker rm -f patient-backend 2>NUL

ECHO 2/4: Bygger Java-projektet
CALL mvnw.cmd clean package -DskipTests

ECHO 3/4: Bygger patient-backend
docker build --no-cache -t patient-backend:latest .

ECHO 4/4: Startar patient-backend
docker run -d --name patient-backend --network patient-network --dns 8.8.8.8 --dns 8.8.4.4 -e "SPRING_DATASOURCE_URL=jdbc:mysql://mysql-db:3306/patient_journal?createDatabaseIfNotExist=true" -e "SPRING_DATASOURCE_USERNAME=root" -e "SPRING_DATASOURCE_PASSWORD=rootpassword" -e "SPRING_JPA_HIBERNATE_DDL_AUTO=update" -p 8080:8080 patient-backend:latest

ECHO Klar