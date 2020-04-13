gRPC Kotlin Server Streaming Cloud Run Example
---------------------------------------

Run Locally:
1. In one shell / terminal window, start the server:
    ```
    ./gradlew run
    ```
1. In another shell / terminal window, run the client:
    ```
    ./gradlew HelloWorldClient
    ```

   You should continually see: `hello, world`

Deploy on Cloud Run:

1. [![Run on Google Cloud](https://deploy.cloud.run/button.svg)](https://deploy.cloud.run)

    *This will take a few minutes to build and deploy.*

1. From within Cloud Shell, run the client against the service you just deployed on Cloud Run, replacing `YOUR_CLOUD_RUN_DOMAIN_NAME` with your service's domain name and replacing `PROJECT_ID` with your GCP project:
   ```
   docker run -it -eHOST=YOUR_CLOUD_RUN_DOMAIN_NAME gcr.io/PROJECT_ID/grpc-hello-world-streaming client
   ```

   You should continually see output like: `Greeter client received: Hello YOUR_CLOUD_RUN_DOMAIN_NAME`
