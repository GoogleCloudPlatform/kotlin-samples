# Google Cloud Pub/Sub Kotlin Sample

[![Open in Cloud Shell][shell_img]][shell_link]

[shell_img]: http://gstatic.com/cloudssh/images/open-btn.svg
[shell_link]: https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googlecloudplatform/kotlin-samples&page=editor&working_dir=pubsub

## Description

[Google Cloud Pub/Sub][pubsub] is a fully-managed real-time messaging service that allows you to send and receive messages between independent applications. This simple Kotlin command-line application demonstrates how to access the Pub/Sub API using
the [Google Cloud Client Library for Java][google-cloud-java].

[pubsub]: https://cloud.google.com/pubsub/
[google-cloud-java]: https://github.com/GoogleCloudPlatform/google-cloud-java


## Quickstart

#### Setup
- [Enable](https://console.cloud.google.com/apis/api/pubsub.googleapis.com/overview) Pub/Sub API.
- Set up [authentication](https://cloud.google.com/docs/authentication/getting-started).

#### Build
- Clone the repository
  ```sh
  git clone https://github.com/GoogleCloudPlatform/kotlin-samples
  cd kotlin-samples/pubsub
  ```
- Build the project with Gradle Wrapper:
  ```sh
  # run with "-info" flag to print potential errors
  ./gradlew build -info
  ```
You should now have a **'pubsub.jar'** file under **build/libs/**
```

#### Create a new topic
```
  java -jar build/libs/pubsub.jar create <topic>
```

#### Create a subscription
```
  java -jar build/libs/pubsub.jar sub <topic> <subscription>
```

#### Publish messages
```
  java -jar build/libs/pubsub.jar pub <topic> <count> 
```

#### Receive messages
```
  java -jar build/libs/pubsub.jar listen <subscription>
```
Subscriber will continue to listen on the topic for 5 minutes and print out message id and data as messages are received.

#### Delete topic
```
  java -jar build/libs/pubsub.jar del-topic <topic>
```

#### Delete subscription
```
  java -jar build/libs/pubsub.jar del-sub <subscription>
```

#### Testing
Run the test with Gradle Wrapper.
```
  ./gradlew test
```
## Contributing changes

* See [CONTRIBUTING.md](../CONTRIBUTING.md)

## Licensing

* See [LICENSE](../LICENSE)
