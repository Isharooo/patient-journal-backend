docker run -d ^
  --name patient-backend ^
  --network patient-network ^
  -p 8080:8080 ^
  patient-journal-backend:latest