gRPC Kotlin Cloud Run Example
-----------------------------

Run Locally:
1. In one shell / terminal window, start the server:
    ```
    ./gradlew run
    ```
1. In another shell / terminal window, run the client:
    ```
    ./gradlew HelloWorldClient
    ```

   You should see output like: `Greeter client received: Hello world`

Deploy on Cloud Run:

1. [![Run on Google Cloud](https://deploy.cloud.run/button.svg)](https://deploy.cloud.run)

    *This will take a few minutes to build and deploy.*

1. Run the client against the service  on Cloud Run:
    ```
    ./gradlew HelloWorldClient --args=YOUR_CLOUD_RUN_DOMAIN_NAME
    ```

   You should see output like: `Greeter client received: Hello YOUR_CLOUD_RUN_DOMAIN_NAME`
