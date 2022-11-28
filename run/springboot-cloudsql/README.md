# Spring Boot CloudSQL

<!-- [![Run on Google Cloud](https://deploy.cloud.run/button.png)](https://deploy.cloud.run) -->

## Local Dev

Run the server:
```
./gradlew bootRun
```

Use the server:
```
# Create a "bar"
curl -v -X POST localhost:8080/bars    -H 'Content-Type: application/json'    -d '{"name": "Test"}'

# Get the "bars"
curl localhost:8080/bars
```


Run the tests:
```
./gradlew test
```


Create container & run with docker:
```
./gradlew bootBuildImage

docker run --rm -ePOSTGRES_PASSWORD=password -p5432:5432 --name my-postgres postgres:13.3

docker run -it --network host \
  -eSPRING_R2DBC_URL=r2dbc:postgresql://localhost/postgres \
  -eSPRING_R2DBC_USERNAME=postgres \
  -eSPRING_R2DBC_PASSWORD=password \
  springboot-cloudsql \
  init

# psql
docker exec -it my-postgres psql -U postgres

docker run -it --network host \
  -eSPRING_R2DBC_URL=r2dbc:postgresql://localhost/postgres \
  -eSPRING_R2DBC_USERNAME=postgres \
  -eSPRING_R2DBC_PASSWORD=password \
  springboot-cloudsql
```

[http://localhost:8080/bars](http://localhost:8080/bars)


## Google Cloud

1. Set project:
    ```
    export PROJECT_ID=YOUR_PROJECT_ID
    ```
2. Create the Cloud SQL instance, VPC, and VPC Connector
3. Create & push the container:
    ```
    ./gradlew bootBuildImage --imageName=gcr.io/$PROJECT_ID/springboot-cloudsql
    docker push gcr.io/$PROJECT_ID/springboot-cloudsql
    ```
4. Init the schema:
    TODO: Need to run the container with an arg `init` and the env vars: `SPRING_R2DBC_URL`, `SPRING_R2DBC_USERNAME`, `SPRING_R2DBC_PASSWORD` in a place that can access the db
5. Deploy the Cloud Run service:
    ```
    gcloud run deploy \
      --image=gcr.io/$PROJECT_ID/springboot-cloudsql \
      --platform=managed \
      --allow-unauthenticated \
      --project=$PROJECT_ID \
      --region=us-central1 \
      --set-env-vars=SPRING_R2DBC_URL=r2dbc:postgresql://YOUR_DB_IP/postgres \
      --set-env-vars=SPRING_R2DBC_USERNAME=YOUR_DB_USERNAME \
      --set-env-vars=SPRING_R2DBC_PASSWORD=YOUR_DB_PASSWORD \
      springboot-cloudsql
    ```
