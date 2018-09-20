Emojify Frontend
===

## Description

Android app written in Kotlin

* Allows User to select an existing picture or take a new picture
* Uploads selected image to bucket in Cloud Storage (see Setup for how to configure bucket)
* Makes a url call to Emojify backend (https://YOUR-PROJECT-ID.appspot.com/emojify?objectName=IMAGE_NAME)
* Processes backend response (parses emojified image public url)
* Downloads and draws emojified image

A few screenshots:

<image src="../screenshots/welcome.png" width="210px"/> <<image src="../screenshots/placeholder-image-1.png" width="210px"/>
<image src="../screenshots/placeholder-image-2.png" width="210px"/> <<image src="../screenshots/result.png" width="210px"/>
## Setup

This app uses Firebase. You will need to:
* Configure Firebase for your Google Cloud Project. Note that it is **REQUIRED** that your android app and the backend are connected to the same Google Cloud Project and that you deploy the backend first! 
* Connect the app to Firebase ([Android Studio](https://developer.android.com/studio/write/firebase) [Manually](https://firebase.google.com/docs/android/setup#manually_add_firebase)). After this step, you should have your Firebase configuration file (google-services.json) present at `frontend/emojify/`
* Edit value of `cloud.project.id` in `application.properties` (src/main/assets): `cloud.project.id = REPLACE_THIS_WITH_YOUR_PROJECT_ID`
* You're all set!
