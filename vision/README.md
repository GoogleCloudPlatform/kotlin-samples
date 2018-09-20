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
## Contributing changes

* See [CONTRIBUTING.md](../CONTRIBUTING.md)

## Licensing

* See [LICENSE](../LICENSE)

[vision-api-docs]: https://cloud.google.com/vision/
[enable-vision-api]: https://console.cloud.google.com/flows/enableapi?apiid=vision.googleapis.com
[google-cloud-java]: https://googlecloudplatform.github.io/google-cloud-java