# Google Vision Kotlin Sample Application

[![Open in Cloud Shell][shell_img]][shell_link]

[shell_img]: http://gstatic.com/cloudssh/images/open-btn.png
[shell_link]: https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googlecloudplatform/kotlin-samples&page=editor&working_dir=vision

## Description

This simple command-line application demonstrates how to invoke Google
Vision API from Kotlin.

## Build and Run
1.  **Clone the repo** and cd into this directory
```
$ git clone https://github.com/GoogleCloudPlatform/kotlin-samples
$ cd kotlin-samples/vision
```
2.  **Enable APIs** - [Enable the Vision API][enable-vision-api] and create a
    new project or select an existing project.

### Using the Command Line / Gradle

To run the sample using the Command Line and [Gradle][gradle], follow these
steps:

1.  **Download The Credentials** - Click "Go to credentials" after enabling the
    APIs. Click "New Credentials" and select "Service Account Key". Create a new
    service account, use the JSON key type, and select "Create". Once
    downloaded, set the environment variable `GOOGLE_APPLICATION_CREDENTIALS` to
    the path of the JSON key that was downloaded.
```
GOOGLE_APPLICATION_CREDENTIALS=/path/to/service_account_credentials.json
```
2.  **Install dependencies** Using the gradle build command.
```
gradle build
```
3.  Run the command!
```sh
# run with the default image file
java -jar build/libs/vision.jar
# run with your own image file
java -jar build/libs/vision.jar path/to/your-image.jpg
```

### Using IntelliJ

1. Install the [Cloud Tools for IntelliJ][cloud-tools-intellij] plugin.
2. Authenticate Google Cloud with the IntelliJ plugin.
3. Open this directory in IntelliJ.
4. Build and run the sample.

## The client library

This sample uses the [Google Cloud Client Library for Java][google-cloud-java].
You can read the documentation for more details on API usage and use GitHub
to [browse the source][google-cloud-java-source] and
[report issues][google-cloud-java-issues].

## Troubleshooting

If you get the error **Failed to detect whether we are running on Google Compute
Engine** or **"The Application Default Credentials are not available"**, set the
environment variable `GOOGLE_APPLICATION_CREDENTIALS` to a path to service
account credentials.

[enable-vision-api]: https://console.cloud.google.com/flows/enableapi?apiid=vision.googleapis.com
[gradle]: https://docs.gradle.org/current/userguide/installation.html
[cloud-tools-intellij]: https://cloud.google.com/tools/intellij/docs/
[google-cloud-java]: https://googlecloudplatform.github.io/google-cloud-java
[google-cloud-java-source]: https://github.com/GoogleCloudPlatform/google-cloud-java
[google-cloud-java-issues]: https://github.com/GoogleCloudPlatform/google-cloud-java/issues

## Contributing changes

* See [CONTRIBUTING.md](../CONTRIBUTING.md)

## Licensing

* See [LICENSE](../LICENSE)
