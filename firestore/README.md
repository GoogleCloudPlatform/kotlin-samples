# Firestore Kotlin Sample

[![Open in Cloud Shell][shell_img]][shell_link]

[shell_img]: http://gstatic.com/cloudssh/images/open-btn.svg
[shell_link]: https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googlecloudplatform/kotlin-samples&page=editor&working_dir=firestore

## Description

This simple command-line application demonstrates how invoke the Google [Cloud Firestore API][firestore-api] from a Kotlin application.

**Check out the sample code in** [quickstart.kt](src/main/quickstart.kt) **and**
[firestore.kt](src/main/firestore.kt).

## Quickstart

#### Setup
- [Enable][enable-firestore-api] Cloud Firestore API inside your Google Cloud project.
- Set up [authentication](https://cloud.google.com/docs/authentication/getting-started).

#### Build
- Clone the repository
  ```sh
  git clone https://github.com/GoogleCloudPlatform/kotlin-samples
  cd kotlin-samples/firestore
  ```
- Build the project with Gradle Wrapper:
  ```sh
  # run with "-info" flag to print potential errors
  ./gradlew build -info
  ```
You should now have a **'firestore.jar'** file under **build/libs/**

#### Running the sample

Usage: ```java -jar firestore.jar YOUR_COLLECTION_NAME [KEY] [VALUE]```

## Contributing changes

* See [CONTRIBUTING.md](../CONTRIBUTING.md)

## Licensing

* See [LICENSE](../LICENSE)

[firestore-api]: https://cloud.google.com/firestore
[enable-firestore-api]: https://console.cloud.google.com/flows/enableapi?apiid=firestore.googleapis.com
