# Google Cloud Vision Kotlin Sample

[![Open in Cloud Shell][shell_img]][shell_link]

[shell_img]: http://gstatic.com/cloudssh/images/open-btn.png
[shell_link]: https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googlecloudplatform/kotlin-samples&page=editor&working_dir=vision

## Description

This simple command-line application demonstrates how to invoke the [Google
Cloud Vision API][vision-api-docs] from Kotlin.

## Build and Run

### Setup

 * **Clone the repo** and cd into this directory:
```sh
git clone https://github.com/GoogleCloudPlatform/kotlin-samples
cd kotlin-samples/vision
```

 * **[Enable the Vision API][enable-vision-api]** and create a new project or
select an existing project.

### Authenticate

 * **Download The Credentials** - Click ["Go to credentials"][create-creds]
   after enabling the APIs. Click "Create Credentials" and select "Service
   Account Key". Create a new service account, use the JSON key type, and select
   "Create". Once downloaded, set the environment variable
   `GOOGLE_APPLICATION_CREDENTIALS` to the path of the JSON key that was
   downloaded.
```sh
GOOGLE_APPLICATION_CREDENTIALS=/path/to/service_account_credentials.json
```

### Install Dependencies with Gradle

 * **Build the project** using the gradle build command:
```sh
# run with "-info" flag to print potential errors
./gradlew build -info
```

### Run the command!

```sh
# Call the Vision API with the default image file in "resources/doggo.jpg"
java -jar build/libs/vision.jar
# Call the Vision API with your own image file
java -jar build/libs/vision.jar path/to/your-image.jpg
```

## Using IntelliJ

This sample can be run in IntelliJ. Install the
[Cloud Tools for IntelliJ][cloud-tools-intellij] plugin, and open the gradle
project.

## The client library

This sample uses the [Google Cloud Client Library for Java][google-cloud-java].
You can read the documentation for more details on API usage and use GitHub
to [browse the source][google-cloud-java-source] and
[report issues][google-cloud-java-issues].

## Troubleshooting

 * **"Failed to detect whether we are running on Google ComputeEngine"**
 * **"The Application Default Credentials are not available"**

If you get these errors, set the environment variable
`GOOGLE_APPLICATION_CREDENTIALS` to a path to service account credentials. If
you don't already have your service account credentials, you will need to
[create them][create-creds].

[vision-api-docs]: https://cloud.google.com/vision/
[enable-vision-api]: https://console.cloud.google.com/flows/enableapi?apiid=vision.googleapis.com
[gradle]: https://docs.gradle.org/current/userguide/installation.html
[create-creds]: https://console.cloud.google.com/apis/credentials
[cloud-tools-intellij]: https://cloud.google.com/tools/intellij/docs/
[google-cloud-java]: https://googlecloudplatform.github.io/google-cloud-java
[google-cloud-java-source]: https://github.com/GoogleCloudPlatform/google-cloud-java
[google-cloud-java-issues]: https://github.com/GoogleCloudPlatform/google-cloud-java/issues

## Contributing changes

* See [CONTRIBUTING.md](../CONTRIBUTING.md)

## Licensing

* See [LICENSE](../LICENSE)
