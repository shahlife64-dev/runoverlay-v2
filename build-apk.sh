#!/usr/bin/env bash
set -e
./gradlew :app:assembleDebug
echo "APK: app/build/outputs/apk/debug/app-debug.apk"
