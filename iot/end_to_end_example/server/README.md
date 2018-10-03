# Google Cloud IoT Sample for Kotlin

[![Open in Cloud Shell][shell_img]][shell_link]

[shell_img]: http://gstatic.com/cloudssh/images/open-btn.svg
[shell_link]: https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googlecloudplatform/kotlin-samples&page=editor&working_dir=iot/end_to_end_example/server

## Description

This directory contains samples for Google Cloud IoT Core API.
[Google Cloud IoT Core][iot_core] allows developers to easily integrate Publish
and Subscribe functionality with devices and programmatically manage device
authorization.

Example:
```sh
java -jar build/libs/server.jar YOUR_PROJECT_ID YOUR_SUBSCRIPTION_NAME
```

[iot_core]: https://cloud.google.com/iot-core

## Quickstart

#### Setup

This sample requires you to have authentication setup. Refer to the
[Authentication Getting Started Guide](https://cloud.google.com/docs/authentication/getting-started)
for instructions on setting up credentials for applications.

Also you will need to enable the [IOT Core API][enable-iotcore-api] and the
[PubSub API][enable-pubsub-api] in your project.

#### Build
- Clone the repository
```sh
git clone https://github.com/GoogleCloudPlatform/kotlin-samples
cd kotlin-samples/iot/end_to_end_example/server
```
- Build the project with Gradle Wrapper:
```sh
# run with "-info" flag to print potential errors
./gradlew build -info
```
#### Running the server

```sh
java -jar build/libs/server.jar YOUR_PROJECT_ID YOUR_SUBSCRIPTION_NAME

```

Where the subscription name is the subscription linked to the IoT device registry
you set up in the [device](../device) portion of this sample.

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

[enable-pubsub-api]: https://console.cloud.google.com/flows/enableapi?apiid=pubsub.googleapis.com
[enable-iotcore-api]: https://console.cloud.google.com/flows/enableapi?apiid=cloudiot.googleapis.com
[gradle]: https://docs.gradle.org/current/userguide/installation.html
[create-creds]: https://console.cloud.google.com/apis/credentials
[cloud-tools-intellij]: https://cloud.google.com/tools/intellij/docs/
[google-cloud-java]: https://googlecloudplatform.github.io/google-cloud-java
[google-cloud-java-source]: https://github.com/GoogleCloudPlatform/google-cloud-java
[google-cloud-java-issues]: https://github.com/GoogleCloudPlatform/google-cloud-java/issues

## Contributing changes

* See [CONTRIBUTING.md](../../../CONTRIBUTING.md)

## Licensing

* See [LICENSE](../../../LICENSE)
