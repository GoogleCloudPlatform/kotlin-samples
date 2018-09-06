# Google Cloud Pub/Sub Kotlin Sample

[![Open in Cloud Shell][shell_img]][shell_link]

<<<<<<< HEAD
[shell_img]: http://gstatic.com/cloudssh/images/open-btn.png
=======
[shell_img]: http://gstatic.com/cloudssh/images/open-btn.svg
>>>>>>> a1f3c22d2611d466ea7e8d896e37cc35b6f056cb
[shell_link]: https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googlecloudplatform/kotlin-samples&page=editor&working_dir=pubsub

## Description

This simple Kotlin command-line application demonstrates how to access the Pub/Sub API using
the [Google Cloud Client Library for Java][google-cloud-java].

[Google Cloud Pub/Sub][pubsub] is a fully-managed real-time messaging service that allows you to
send and receive messages between independent applications.

[pubsub]: https://cloud.google.com/pubsub/
[google-cloud-java]: https://github.com/GoogleCloudPlatform/google-cloud-java

## Quickstart

#### Setup
- Install [Maven](http://maven.apache.org/).
- [Enable](https://console.cloud.google.com/apis/api/pubsub.googleapis.com/overview) Pub/Sub API.
- Set up [authentication](https://cloud.google.com/docs/authentication/getting-started).

#### Build
- Build your project with:
```
  mvn clean package -DskipTests
```

#### Create a new topic
```
  mvn exec:java -Dexec.args="create my-topic"
```

#### Create a subscription
```
  mvn exec:java -Dexec.args="sub my-topic my-sub"
```

#### Publish messages
```
  mvn exec:java -Dexec.args="pub my-topic 5"
```
Publishes 5 messages to the topic `my-topic`.

#### Receive messages
```
   mvn exec:java -Dexec.args="listen my-sub"
```
Subscriber will continue to listen on the topic for 5 minutes and print out message id and data as messages are received.

#### Testing
Run the test with Maven.
```
  mvn verify
```
