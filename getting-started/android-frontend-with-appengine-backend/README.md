Serverless Kotlin: Getting Started App
===

This is a sample application to demonstrate a Kotlin backend which communicates with a mobile
frontend. The backend emojifies people faces in an input image by overlaying on detected faces, emojis corresponding to predicted emotions. The sample is called **Emojify**.

Example of source and emojified images:

<image src="screenshots/meetup.jpg" width="430px"/> <<image src="screenshots/emojified-meetup.jpg" width="430px"/>
These pictures were taken at [DroidCon NYC Extended](https://dcnyc-extended-2018.splashthat.com/) in NYC.
            
## Cloud Client Libraries
The app integrates two Google Cloud Client Libraries:
* [Google Cloud Storage](https://cloud.google.com/storage): hosts input images
* [Google Cloud Vision](https://cloud.google.com/vision): used to perform face detection on input image

## Ideal workflow:
* User takes a picture with frontend app
* Frontend sends picture to a bucket in Google Cloud Storage
* Frontend calls Emojify backend and specifies path to picture in bucket
* Backend emojifies image and returns a response containing public url of emojified image

Having a frontend app is however totally optional as one can directly call the backend with a path to an image in a Storage bucket.

## Code
This folder contains two sub-folders:

|Link|Description|
|---|---|
|[Emojify frontend](Frontend/)|Android application written in Kotlin|
|[Emojify backend](Backend/)|SpringBoot Kotlin app to be deployed on App Engine|
