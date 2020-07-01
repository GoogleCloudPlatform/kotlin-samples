Hello Kotlin Ktor
-----------------

## Run Locally:
```
./gradlew dev
```

## Click-to-Deploy on Google Cloud Run
[![Run on Google Cloud](https://deploy.cloud.run/button.svg)](https://deploy.cloud.run)

## CLI Deploy on Google Cloud Run
1. [Install & setup gcloud](https://cloud.google.com/sdk/install)
1. Build the container image on Cloud Build using Buildpacks, storing the image on Google Container Registry:
```
export PROJECT_ID=YOUR_GCP_PROJECT
gcloud alpha builds submit --pack=image=gcr.io/$PROJECT_ID/ktor-hello-world
```
1. Deploy the container on Cloud Run:
```
gcloud run deploy ktor-hello-world --project=$PROJECT_ID --region=us-central1 --platform=managed --allow-unauthenticated --image=gcr.io/$PROJECT_ID/ktor-hello-world
```
