Emojify Frontend
===

## Description
Android app written in Kotlin. The app prompts user to select a picture after which it:
* Uploads image to bucket in Cloud Storage (see Setup for how to configure bucket)
* Makes a url call to Emojify backend (https://YOUR-PROJECT-ID.appspot.com/emojify?objectName=IMAGE_NAME)
* Processes backend response (parses emojified image public url)
* Downloads and draws emojified image

A few screenshots:

<image src="../screenshots/welcome.png" width="210px"/> <<image src="../screenshots/placeholder-image-1.png" width="210px"/>
<image src="../screenshots/placeholder-image-2.png" width="210px"/> <<image src="../screenshots/result.png" width="210px"/>
## Setup

This app uses Firebase. You will need to:
* Configure Firebase for your Google Cloud Project. Note that it is **REQUIRED** that your android app and the backend are connected to the same Google Cloud Project and that you deploy the backend first! 
* Make sure your Firebase configuration file (google-services.json) is downloaded to Frontend/emojify/
* Edit value of *storage.bucket.name* in **application.properties** (src/main/assets): `storage.bucket.name = REPLACE_THIS_WITH_YOUR_BUCKET`
* You're all set!
