Spring Boot Kotlin Reactive Demo
--------------------------------

Run Locally (dev mode):
1. In one terminal: `./gradlew -t classes`
1. In another terminal: `./gradlew bootRun`
1. Open: [localhost:8080](http://localhost:8080)

Deploy on Cloud Run (with a couple clicks):
[![Run on Google Cloud](https://deploy.cloud.run/button.svg)](https://deploy.cloud.run)

Run on Google Cloud Run (with the command line):

1. Create Docker Image for GCP:
    ```
    export PROJECT_ID=YOUR_GCP_PROJECT_ID
    ./gradlew bootBuildImage --imageName=gcr.io/$PROJECT_ID/springboot-hello-world
    docker push gcr.io/$PROJECT_ID/springboot-hello-world
    ```

1. Run Docker Image Locally:
    ```
    docker run -p8080:8080 gcr.io/$PROJECT_ID/springboot-hello-world
    ```

1. Deploy on Google Cloud Run:
    ```
    gcloud run deploy \
      --image=gcr.io/$PROJECT_ID/springboot-kotlin-reactive-demo \
      --platform=managed \
      --allow-unauthenticated \
      --project=$PROJECT_ID \
      --region=us-central1 \
      springboot-hello-world
    ```
