WeSign are application assistant for sign language using computer vision and machine learning to continuously predict image for sign language detection. 
The model files are downloaded via Gradle scripts when you build and run the app. You don't need to do any steps to download TFLite models into the project explicitly.

## Features

There are 3 Main Features:
- Analyzer
- TextToSign
- Learning:

### Screenshot


## Build Project

### Prerequisites
- The **[Android Studio](https://developer.android.com/studio/index.html)** IDE. This project has been tested on Android Studio Flamingo 2022.2.1 Patch 2
- A physical Android device with a minimum OS version of SDK 26 (Android 8.0 - Oreo) with developer mode enabled. The process of enabling developer mode may vary by device.

### Requirement
- Android Studio
- JDK 17

### Installation

1. Clone it using command below:
```bash
git clone https://github.com/WeSign-Github/Android-WeSign.git
```
or you can download and extract it

2. Open Android studio and open project and target it to cloned/extracted folder location.

### Model
Downloading model are automatically handled by download.gradle when building project. After building project you can find 2 `.tfLite` model at `app\src\main\assets` 