Serverless Kotlin: Getting Started App
===

This is a sample application to demonstrate a Kotlin backend which communicates with a mobile frontend.

The backend receives an image and for each face detected:
* predicts emotion from facial expression
* overlays emoji corresponding to predicted emotion

The sample is called **Emojify**.

Example of source and emojified images:

<image src="screenshots/meetup.jpg" width="430px"/> <<image src="screenshots/emojified-meetup.jpg" width="430px"/>
Picture taken at [DroidCon NYC Extended](https://dcnyc-extended-2018.splashthat.com/).
            
### Cloud Client Libraries
The app integrates two Google Cloud Client Libraries:
* [Google Cloud Storage](https://cloud.google.com/storage): hosts input and emojified images
* [Google Cloud Vision](https://cloud.google.com/vision): used to perform face detection and image annotation on input image

### Emojify workflow:
* User takes a picture with frontend app
* Frontend sends picture to a bucket in Google Cloud Storage
* Frontend calls Emojify backend and specifies path to picture in bucket
* Backend emojifies image and returns a response containing public url of emojified image

Having a frontend app is however totally optional as one can directly call the backend with a path to an image in a Storage bucket.

### Code
This folder contains two sub-folders:

|Link|Description|
|---|---|
|[Emojify frontend](frontend/)|Android application written in Kotlin|
|[Emojify backend](backend/)|SpringBoot Kotlin app to be deployed on App Engine|

### Tutorial

This [codelab](https://g.co/codelabs/emojify) walks you through how to build and deploy Emojify.
