# Telemetry Runner V1 — One-click APK project

## Fastest way to build the debug APK

Open this folder in Android Studio. Let Android Studio install/sync the required
Android SDK components, then choose:

Build → Build App Bundle(s) / APK(s) → Build APK(s)

The debug APK will be produced at:

app/build/outputs/apk/debug/app-debug.apk

The APK is debug-signed and can be installed directly on an Android device.

## Requirements
- Android Studio Ladybug or newer
- Android SDK Platform 35
- JDK 17 (Android Studio bundled JDK is recommended)
- Internet connection for the first Gradle dependency download

## Current V1 functionality
- Video selection
- FIT/GPX/TCX file selection
- Video playback
- Overlay preview
- Synchronization offset control

## Not yet implemented
- FIT/GPX/TCX parsing
- Timestamp interpolation
- Actual overlay burn-in
- MP4 export
- Drag-and-drop overlay editor
