# gRPC Kotlin Android Sample

## Local Dev

Run the server:
```
./gradlew -t :server:run
```

Run the client:

1. [Download Android Command Line Tools:](https://developer.android.com/studio)

1. Install the SDK:
    ```
    mkdir android-sdk
    cd android-sdk
    unzip PATH_TO_SDK_ZIP/sdk-tools-linux-VERSION.zip
    tools/bin/sdkmanager --update
    tools/bin/sdkmanager "platforms;android-29" "build-tools;29.0.3" "extras;google;m2repository" "extras;android;m2repository"
    tools/bin/sdkmanager --licenses
    ```

1. Add the following to your ~/.bashrc
    ```
    export ANDROID_SDK_ROOT=PATH_TO_SDK/android-sdk
    ```

1. Source the new profile:
    ```
    source ~/.bashrc
    ```

1. *BUG WORKAROUND*: Create a `local.properties` in the project root containing:
    ```
    sdk.dir=YOUR_ANDROID_SDK_ROOT
    ```

1. Run the build from this project's dir:
    ```
    ./gradlew :android:build
    ```

1. For a physical device, [setup adb](https://developer.android.com/studio/run/device)

1. Run on a device using an external server:
    ```
    ./gradlew :android:installDebug -PserverUrl=https://YOUR_SERVER/
    ```

1. Or to run from Android Studio / IntelliJ, create a `gradle.properties` file in your root project directory containing:
    ```
   serverUrl=http://YOUR_SERVER:50051/
    ```


Deploy on Cloud Run:

[![Run on Google Cloud](https://deploy.cloud.run/button.png)](https://deploy.cloud.run/?cloudshell_context=cloudrun-gbp)
