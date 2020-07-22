gRPC Kotlin Cloud Run Example
-----------------------------

Run Locally:
1. In one shell / terminal window, start the server:
    ```
    ./mvnw compile exec:java -Dexec.mainClass="io.grpc.examples.helloworld.HelloWorldServerKt"
    ```
1. In another shell / terminal window, run the client:
    ```
    ./mvnw compile exec:java -Dexec.mainClass="io.grpc.examples.helloworld.HelloWorldClientKt"
    ```

   You should see output like: `Greeter client received: Hello world`

Deploy on Cloud Run:

1. [![Run on Google Cloud](https://deploy.cloud.run/button.svg)](https://deploy.cloud.run)

    *This will take a few minutes to build and deploy.*

1. From within Cloud Shell, run the client against the service you just deployed on Cloud Run, replacing `YOUR_CLOUD_RUN_DOMAIN_NAME` with your service's domain name and replacing `YOUR_PROJECT_ID` with your GCP project:
   ```
   export PROJECT_ID=YOUR_PROJECT_ID
   docker run -it gcr.io/$PROJECT_ID/grpc-hello-world-mvn \
   "java -cp target/classes:target/dependency/* io.grpc.examples.helloworld.HelloWorldClientKt YOUR_CLOUD_RUN_DOMAIN_NAME"
   ```

   You should see output like: `Greeter client received: Hello YOUR_CLOUD_RUN_DOMAIN_NAME`
