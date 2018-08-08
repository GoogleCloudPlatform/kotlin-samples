App Engine Getting Started Kotlin application: Emojify Backend
===

## Sample SpringBoot application written in Kotlin for use with App Engine Java8 Standard.

This is a sample application to demonstrate a Kotlin backend which communicates with a mobile
frontend.

See the [Google App Engine standard environment documentation][ae-docs] for more
detailed instructions.

[ae-docs]: https://cloud.google.com/appengine/docs/java/

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven](https://maven.apache.org/download.cgi) (at least 3.5)
* [Google Cloud SDK](https://cloud.google.com/sdk/) (aka gcloud command line tool)

## About the sample
This sample emojifies an input image by swapping faces detected on the image with emojis corresponding to predicted emotions on the faces. Face dectection is performed via [Cloud Vision API](https://cloud.google.com/vision).

This backend acts as a cloud endpoint:

* Input: **objectName** (String). Corresponds to an image in **gs://cloud-kotlin-samples** (bucket editable in source code)
* Output: a String corresponding to the **public url** of the emojified image

## Live demo

Backend endpoint: [https://cloud-kotlin-samples.appspot.com/emojify](https://cloud-kotlin-samples.appspot.com/emojify)

Example of call: https://cloud-kotlin-samples.appspot.com/emojify?objectName=engineers.png

## Setup

* Download and initialize the [Cloud SDK](https://cloud.google.com/sdk/)

    `gcloud init`

* Create an App Engine app within the current Google Cloud Project

    `gcloud app create`

## Maven
### Running locally

`mvn appengine:run`

To use visit: http://localhost:8080/

### Deploying

`mvn appengine:deploy`

To use vist:  https://YOUR-PROJECT-ID.appspot.com

## Testing

`mvn verify`

As you add / modify the source code (`src/main/java/...`) it's very useful to add [unit testing](https://cloud.google.com/appengine/docs/java/tools/localunittesting)
to (`src/main/test/...`).  The following resources are quite useful:

* [Junit4](http://junit.org/junit4/)
* [Mockito](http://mockito.org/)
* [Truth](http://google.github.io/truth/)


For further information, consult the
[Java App Engine](https://developers.google.com/appengine/docs/java/overview) documentation.

