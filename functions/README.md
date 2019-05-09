# Using Kotlin with Google Cloud Functions

**Cloud Functions with Java 8 is currently in Alpha!**

[Sign up here](https://docs.google.com/forms/d/e/1FAIpQLScC98jGi7CfG0n3UYlj7Xad8XScvZC8-BBOg7Pk3uSZx_2cdQ/viewform)
to request alpha access.

## Deploying Kotlin Cloud Function

When deploying to Google Cloud Functions, you can deploy functions for
HTTP triggers or for PubSub messages.

### Http Trigger

An example function for HTTP triggers resides in [`src/main/kotlin/HttpExample.kt`](src/main/kotlin/HttpExample.kt).
Deploy it with the following `gcloud` command:

```sh
gcloud functions deploy http-example \
    --runtime=java8 \
    --entry-point=HttpExample.helloWorld \
    --trigger-http \
    --source=.
```

The dependencies are built automatically using `pom.xml`. If you'd like to
use Gradle instead, see `Deploy a JAR` below.

Now you can make a request to the returned URL to see the "Hello World" response:

```sh
$ curl "https://[YOUR_ZONE]-[YOUR_PROJECT_ID].cloudfunctions.net/http-example"
Hello, World!
```

You can see the URL for your function by running the following:

```sh
gcloud functions describe http-example | grep url
```

Where "jar-example" is the name of your Cloud Function.

### Pub/Sub Trigger

An example function for Pub/Sub resides in [`src/main/kotlin/EventExample.kt`](src/main/kotlin/EventExample.kt). Before you deploy it, you need to create a Pub/Sub topic. This example uses
`hello-topic`:

```sh
gcloud pubsub topics create hello-topic
```

Now deploy the Cloud Function with the following `gcloud` command:

```sh
gcloud functions deploy pubsub-example \
    --runtime java8 \
    --entry-point EventExample.helloPubSub \
    --trigger-resource hello-topic \
    --trigger-event google.pubsub.topic.publish
```

Now you can trigger a call to the `pubsub-example` function and pass in
base64-encoded data. The data below says "Hello, Pub/Sub!"

```sh
gcloud functions call pubsub-example --data '{"data":"SGVsbG8sIFB1Yi9TdWIh"}'
```

Now if you open the [Stackdriver Logging UI](https://console.cloud.google.com/logs),
or call the `gcloud functions logs read` command, you will see the output:

```sh
$ gcloud functions logs read pubsub-example
NAME            EXECUTION_ID  TIME_UTC                 LOG
pubsub-example  wss5cki38tkb  2019-05-08 21:24:28.061  INFO: Hello, Pub/Sub!
```

### Deploy a JAR (required for building with Gradle)

Google Cloud Functions support deploying pre-built fat JARs. This is especially
useful when building functions with Gradle, as Cloud Functions does not
currently support building from Gradle automatically. A fat JAR is built
automatically using the `shadowJar` task when you run `gradle build`:

```
apply plugin: 'com.github.johnrengelman.shadow'
tasks.build.dependsOn tasks.shadowJar
```

Now you can build a fat JAR file:

```sh
./gradlew build
```

To deploy the JAR, it must be in directory by itself:

```sh
mkdir jar-deployment
cp build/libs/functions-all.jar jar-deployment/
```

Now you can deploy to Google Cloud Functions by providing the directory
your JAR resides in as the source:

```sh
gcloud functions deploy jar-example \
    --runtime=java8 \
    --entry-point=HttpExample.helloWorld \
    --trigger-http \
    --source=jar-deployment/
```


