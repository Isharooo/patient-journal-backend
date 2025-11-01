docker run -d ^
  --name mysql-db ^
  --network patient-network ^
  -e MYSQL_ROOT_PASSWORD=rootpassword ^
  -e MYSQL_DATABASE=patient_journal ^
  -p 3306:3306 ^
  mysql:8.0