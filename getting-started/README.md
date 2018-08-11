Getting Started with Kotlin on Server Side: A Sample App
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
This sample app emojifies people faces in an input image (stored in the cloud via [Google Cloud Storage](https://cloud.google.com/storage)) by swapping faces detected on the image with emojis corresponding to predicted emotions. Face dectection is performed via [Cloud Vision API](https://cloud.google.com/vision).

When deployed on App Engine, the backend acts as a cloud endpoint:

* Input: **objectName** (String). A path correspoinding to an object in your bucket.
* Output: object of class **[EmojifyResponse](src/main/kotlin/com/google/cloud/kotlin/emojify/EmojifyController.kt)**

## Live demo

Backend endpoint: [https://cloud-kotlin-samples.appspot.com/emojify](https://cloud-kotlin-samples.appspot.com/emojify)

Sample calls: 
* https://cloud-kotlin-samples.appspot.com/emojify?objectName=engineers.png or 
* https://cloud-kotlin-samples.appspot.com/emojify?objectName=for-the-people.jpeg

Sample responses:
* {"objectPath":"emojified/emojified-engineers.png","emojifiedUrl":"https://storage.googleapis.com/cloud-kotlin-samples/emojified/emojified-engineers.png","statusCode":"OK","errorCode":null,"errorMessage":null}
* {"objectPath":"emojified/emojified-for-the-people.jpeg","emojifiedUrl":"https://storage.googleapis.com/cloud-kotlin-samples/emojified/emojified-for-the-people.jpeg","statusCode":"OK","errorCode":null,"errorMessage":null}

## Setup

* Download and initialize the [Cloud SDK](https://cloud.google.com/sdk/)

    `gcloud init`

* Create an App Engine app within the current Google Cloud Project

    `gcloud app create`
* Create file **application.properties** in **src/main/resources**.

* Create a Google Cloud bucket and add this line to application.properties: `storage.bucket.name = [your-bucket]`.

## Maven
### Running locally

`mvn appengine:run`

To use visit: http://localhost:8080/

### Deploying on App Engine

`mvn appengine:deploy`

To use visit:  https://YOUR-PROJECT-ID.appspot.com

## Testing

`mvn verify`

As you add / modify the source code (`src/main/kotlin/...`) it's very useful to add [unit testing](https://cloud.google.com/appengine/docs/java/tools/localunittesting)
to (`src/main/test/...`).  The following resources are quite useful:

* [Junit4](http://junit.org/junit4/)
* [Mockito](http://mockito.org/)
* [Truth](http://google.github.io/truth/)


For further information, consult the
[Java App Engine](https://developers.google.com/appengine/docs/java/overview) documentation.

