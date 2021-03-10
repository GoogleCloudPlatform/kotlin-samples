Hello World
-----------

## Run Locally:
1. Start the local server:
    ```
    ./mvnw compile exec:java
    ```
1. Open: [localhost:8080](http://localhost:8080)

## Click-to-Deploy on Google Cloud Run
[![Run on Google Cloud](https://deploy.cloud.run/button.svg)](https://deploy.cloud.run)

## CLI Deploy on Google Cloud Run
1. [Install & setup gcloud](https://cloud.google.com/sdk/install)

1. Enable the Container, Container Registry, Cloud Build, and Cloud Run APIs:
    ```
    gcloud services enable container.googleapis.com containerregistry.googleapis.com cloudbuild.googleapis.com run.googleapis.com
    ```

1. Build the container image and store it on Google Container Registry:
    ```
    export PROJECT_ID=YOUR_GCP_PROJECT
    ./mvnw compile jib:build -Dimage=gcr.io/$PROJECT_ID/plain-hello-world
    ```

1. Deploy the container on Cloud Run:
    ```
    gcloud run deploy \
      --project=$PROJECT_ID \
      --region=us-central1 \
      --platform=managed \
      --allow-unauthenticated \
      --image=gcr.io/$PROJECT_ID/plain-hello-world \
      plain-hello-world
    ```

## Local Docker Build & Run

1. [Install Docker](https://docs.docker.com/get-docker/)

1. Build the image using Jib:
    ```
    ./mvnw compile jib:dockerBuild -Dimage=plain-hello-world
    ```

1. Run image:
    ```
    docker run -p8080:8080 plain-hello-world
    ```

1. Open: [localhost:8080](http://localhost:8080)
