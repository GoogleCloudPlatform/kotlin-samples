# Google Cloud Vision Kotlin Sample

[![Open in Cloud Shell][shell_img]][shell_link]

[shell_img]: http://gstatic.com/cloudssh/images/open-btn.svg
[shell_link]: https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googlecloudplatform/kotlin-samples&page=editor&working_dir=vision

## Description

This simple command-line application demonstrates how to invoke the [Google
Cloud Vision API][vision-api-docs] from Kotlin. The sample calls the Vision API on an input image.

Example:
```sh 
java -jar build/libs/vision.jar ./resources/doggo.jpg
```

## Quickstart

#### Setup
- [Enable][enable-vision-api] Cloud Vision API.
- Set up [authentication](https://cloud.google.com/docs/authentication/getting-started).

#### Build
- Clone the repository
```sh
git clone https://github.com/GoogleCloudPlatform/kotlin-samples
cd kotlin-samples/vision
```
- Build the project with Gradle Wrapper:
```sh
# run with "-info" flag to print potential errors
./gradlew build -info
```
#### Running the sample!

```sh
# Call the Vision API with the default image file in "resources/doggo.jpg"
java -jar build/libs/vision.jar
# Call the Vision API with your own image file
java -jar build/libs/vision.jar path/to/your-image.jpg
```

## Using IntelliJ IDE

This sample can be run in IntelliJ. You will need to install the
[Cloud Tools for IntelliJ][cloud-tools-intellij] plugin and open the gradle
project.

## About The Client Library

This sample uses the [Google Cloud Client Library for Java][google-cloud-java].
You can read the documentation for more details on API usage and use GitHub
to [browse the source][google-cloud-java-source] and
[report issues][google-cloud-java-issues].

## Troubleshooting
 * **"Failed to detect whether we are running on Google ComputeEngine"**; **"The Application Default Credentials are not available"**: If you get these errors, make sure you set the environment variable `GOOGLE_APPLICATION_CREDENTIALS` to a path to your service account credentials. If you don't already have service account credentials, you will need to
[create them][create-creds]. 

 * If you get any **"Permission denied"** error, make sure you enabled billing for your Google Cloud project. 
 
 * **IntelliJ:** If you get this error: *"...the provided plugin org.jetbrains.kotlin.scripting.compiler.plugin.Scripting CompilerConfiguration ComponentRegistrar is not compatible with this version of compiler"*, **manually update your IDE Kotlin Plugin**.
 
 * **IntelliJ:** If you get *"The project is not backed up by gradle"* or *"couldn't find jdk"* errors, make sure you set Gradle home path correctly -> [How to set Gradle home in IntelliJ](https://www.jetbrains.com/help/idea/gradle-settings.html).

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
