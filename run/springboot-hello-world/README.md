Spring Boot + Kotlin Hello, World
---------------------------------

## Run Locally:
1. Start the local server: `./gradlew bootRun`
1. Open: [localhost:8080](http://localhost:8080)

## Deploy on Cloud Run (with a couple clicks):
[![Run on Google Cloud](https://deploy.cloud.run/button.svg)](https://deploy.cloud.run)

## Run on Google Cloud Run (with the command line):

1. [Install & setup gcloud](https://cloud.google.com/sdk/install)

1. Enable the Container, Container Registry, Cloud Build, and Cloud Run APIs:
    ```
    gcloud services enable container.googleapis.com containerregistry.googleapis.com cloudbuild.googleapis.com run.googleapis.com
    ```

1. Build the container image and push it to the Google Container Registry:
    ```
    export PROJECT_ID=YOUR_GCP_PROJECT_ID
    ./gradlew bootBuildImage --imageName=gcr.io/$PROJECT_ID/springboot-hello-world

    docker push gcr.io/$PROJECT_ID/springboot-hello-world
    ```

1. Deploy on Google Cloud Run:
    ```
    gcloud run deploy \
      --image=gcr.io/$PROJECT_ID/springboot-hello-world \
      --platform=managed \
      --allow-unauthenticated \
      --project=$PROJECT_ID \
      --region=us-central1 \
      springboot-hello-world
    ```

## Local Docker Build & Run

1. [Install Docker](https://docs.docker.com/get-docker/)

1. Build the image
    ```
    ./gradlew bootBuildImage --imageName=springboot-hello-world
    ```

1. Run image:
    ```
    docker run -p8080:8080 springboot-hello-world
    ```

1. Open: [localhost:8080](http://localhost:8080)