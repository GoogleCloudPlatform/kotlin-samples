Emojify Backend
===

## Sample SpringBoot application written in Kotlin for use with App Engine Java8 Standard.
This folder contains the source code of Emojify backend. When deployed on App Engine, the backend acts as a cloud endpoint:
* Input: path to image in configured bucket (see setup)
* Output: object of class [EmojifyResponse](src/main/kotlin/com/google/cloud/kotlin/emojify/EmojifyController.kt)

## Live demo

Backend endpoint: [https://cloud-kotlin-samples.appspot.com/emojify](https://cloud-kotlin-samples.appspot.com/emojify)

Sample calls: 
* https://cloud-kotlin-samples.appspot.com/emojify?objectName=engineers.png 
* https://cloud-kotlin-samples.appspot.com/emojify?objectName=for-the-people.jpeg

Sample responses:
* {"objectPath":"emojified/emojified-engineers.png","emojifiedUrl":"https://storage.googleapis.com/cloud-kotlin-samples/emojified/emojified-engineers.png","statusCode":"OK","errorCode":null,"errorMessage":null}
* {"objectPath":"emojified/emojified-for-the-people.jpeg","emojifiedUrl":"https://storage.googleapis.com/cloud-kotlin-samples/emojified/emojified-for-the-people.jpeg","statusCode":"OK","errorCode":null,"errorMessage":null}

## Setup

* Download and initialize the [Cloud SDK](https://cloud.google.com/sdk/)

    `gcloud init`

* Create an App Engine app within the current Google Cloud Project

    `gcloud app create`

* Edit value of *storage.bucket.name* in **application.properties** (src/main/resources): `storage.bucket.name = REPLACE_THIS_WITH_YOUR_BUCKET`.

## Maven
### Running locally

`mvn appengine:run`

To use visit: http://localhost:8080/

### Deploying to App Engine

`mvn appengine:deploy`

To use visit:  https://YOUR-PROJECT-ID.appspot.com

See the [Google App Engine standard environment documentation][ae-docs] for more
detailed instructions.

[ae-docs]: https://cloud.google.com/appengine/docs/java/

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven](https://maven.apache.org/download.cgi) (at least 3.5)
* [Google Cloud SDK](https://cloud.google.com/sdk/) (aka gcloud command line tool)

## Testing

`mvn verify`

As you add / modify the source code (`src/main/kotlin/...`) it's very useful to add [unit testing](https://cloud.google.com/appengine/docs/java/tools/localunittesting)
to (`src/main/test/...`).  The following resources are quite useful:

* [Junit4](http://junit.org/junit4/)
* [Mockito](http://mockito.org/)
* [Truth](http://google.github.io/truth/)


For further information, consult the
[Java App Engine](https://developers.google.com/appengine/docs/java/overview) documentation.

