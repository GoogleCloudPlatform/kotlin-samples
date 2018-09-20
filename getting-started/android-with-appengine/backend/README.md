Emojify Backend
===

## Sample SpringBoot application written in Kotlin for use with App Engine Java8 Standard.
This folder contains the source code of Emojify backend. When deployed on App Engine, the backend acts as a cloud endpoint:
* Input: name of source image in configured bucket (see setup)
* Output: object of class [EmojifyResponse](src/main/kotlin/com/google/cloud/kotlin/emojify/EmojifyController.kt)

## Setup

* Download and initialize the [Cloud SDK](https://cloud.google.com/sdk/)

    `gcloud init`

* Edit value of *storage.bucket.name* in **application.properties** (src/main/resources): `storage.bucket.name = REPLACE_THIS_WITH_YOUR_BUCKET`.

## Maven
### Running locally

`mvn appengine:run`

To use visit: http://localhost:8080/

### Deploying to App Engine

`mvn appengine:deploy`

See the [Google App Engine standard environment documentation][ae-docs] for more
detailed instructions.

[ae-docs]: https://cloud.google.com/appengine/docs/java/

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven](https://maven.apache.org/download.cgi) (at least 3.5)
* [Google Cloud SDK](https://cloud.google.com/sdk/) (aka gcloud command line tool)

## Testing

`mvn verify`