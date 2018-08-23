# Firestore Kotlin Sample

[![Open in Cloud Shell][shell_img]][shell_link]

[shell_img]: http://gstatic.com/cloudssh/images/open-btn.png
[shell_link]: https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googlecloudplatform/kotlin-samples&page=editor&working_dir=firestore

## Description

This simple command-line application demonstrates how to talk to
[Firestore][firestore-docs] with Kotlin. The sample calls the Firestore API.

Example:
```sh
java -jar build/libs/firestore.jar YOUR_COLLECTION_ID key value
```

## Build and Run

### Enabling Firestore API

Follow this **[link][enable-firestore-api]** to enable the **Firestore API** within an existing Google Cloud project or in a new project.

### Setting up authentication

For the [Google
Firestore API][firestore-api-docs] to work, you must first setup **[authentication](https://cloud.google.com/docs/authentication/production)** by creating a service account key.

1. After enabling the Firestore API, [Go to credentials][create-creds] and click **Create credentials**.
2. From the **Create credentials** drop-down list, select **Service account key**.
3. From the **Service account** drop-down list, select **New service account**.
4. Choose a name for your Service account.
4. No role is required to access this service unless your project has specific requirements. In this case, select the role type that best fits your needs. Otherwise, do not select any role.
5. Select **JSON** Key type and click **Create**. Note: if you didn't select any role, a warning will appear; click **Create without role**.
6. A JSON file that contains your credentials will be **downloaded to your computer**.

### Setting the `GOOGLE_APPLICATION_CREDENTIALS` environment variable

Set the environment variable **GOOGLE_APPLICATION_CREDENTIALS** to the path of the JSON file downloaded previously.
**Note:** This variable only applies to your current shell session, so if you open a new session, set the variable again.

* Linux or macOS:
```sh
export GOOGLE_APPLICATION_CREDENTIALS=/path/to/service_account_key.json
```
* Windows:

   With command prompt:
   ```sh
   set GOOGLE_APPLICATION_CREDENTIALS= /path/to/service_account_key.json
   ```
   With PowerShell:
   ```sh
   $env:GOOGLE_APPLICATION_CREDENTIALS= '/path/to/service_account_key.json'
   ```

### Cloning the repository
```sh
git clone https://github.com/GoogleCloudPlatform/kotlin-samples
cd kotlin-samples/firestore
```
### Installing dependencies with Gradle

**Build the project** using the gradle build command:
```sh
# run with "-info" flag to print potential errors
./gradlew build -info
```

### Running the sample!

Replace `YOUR_COLLECTION_ID` below with your Dataset ID.

```sh
# Store an arbitrary key/value in your dataset
java -jar build/libs/firestore.jar YOUR_COLLECTION_ID key value
# Retrieve the value for a given key in your dataset
java -jar build/libs/firestore.jar YOUR_COLLECTION_ID key

```

## Using IntelliJ IDE

This sample can be run in IntelliJ. You will need to install the
[Cloud Tools for IntelliJ][cloud-tools-intellij] plugin and open the gradle
project.

## The client library

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

[firestore-docs]: https://cloud.google.com/firestore
[enable-firestore-api]: https://console.cloud.google.com/flows/enableapi?apiid=firestore.googleapis.com
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
