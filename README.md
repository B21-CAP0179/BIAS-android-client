# Body-Imaging-Anomaly-Scanner

Capstone Project for Bangkit Academy 2021

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Peduli-lindungi.id is a one-stop service developed by the Indonesian Ministry of Communication and Information to aggregate Indonesian citizen's effort to combat healthcare emergencies (including COVID-19) and provide relevant information to Indonesian citizen. Body-Imaging-Anomaly-Scanner is a proposed feature within Peduli-lindungi.id that can detect whether there is any disease within a patient body through medical images, such as MRI results.

## User Requirements

- An Android device running at minimum Android 5.0 / Lollipop (API Level 21)
- Internet connection in said device

## Required Permissions

- Internet connection (to communicate with BIAS servers)
- Access to storage system (to upload images for anomaly screening)

## Developer / Maintainer Requirements

- Android SDK targeting Android 11 (API Level 30)
- Kotlin SDK >= 1.5
- Android Studio >=4.1 (recommended)

## Application Dependencies

In addition to Android / Android Jetpack libraries, this application also uses libraries as follows:

- [Firebase Android SDK by Google](https://firebase.google.com/docs/android/setup)
- [CircleImageView by hdodenhof](https://github.com/hdodenhof/CircleImageView)
- [Glide by Bumptech](https://github.com/bumptech/glide)
- [Retrofit by Square](https://square.github.io/retrofit/)

The following dependencies statement is used inside this application's Gradle build script:

~~~kotlin
dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    implementation 'androidx.core:core-ktx:1.5.0'
    implementation 'androidx.appcompat:appcompat:1.3.0'
    implementation 'com.google.android.material:material:1.3.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    implementation 'com.google.firebase:firebase-auth:21.0.1'
    implementation 'com.google.firebase:firebase-firestore:23.0.1'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation 'de.hdodenhof:circleimageview:3.1.0'
    implementation 'com.google.firebase:firebase-storage:20.0.0'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    implementation 'com.github.bumptech.glide:glide:4.12.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
}
~~~

How to replicate our application:
1. Build model exploration so we can use it for the web server
2. Downloading and saving the model use for the prediction image
3. Build webserver using Flask
4. Preparing dockerfile to build docker image 
5. Preparing container registry for docker image 
6. Pushing docker image to Container registry
7. Granting permission to Cloud Run through IAM & Admin in GCP
8. Preparing Cloud Run for deploying image from container registry
9. Preparing Cloud build for automatic deployment so we can use continues deployment
10. Preparing Firestore/Firebase for the database
11. Design the Android Application
12. Connect Firestore/Firebase to the Android Studio

