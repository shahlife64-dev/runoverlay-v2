@echo off
setlocal
if not exist "%~dp0gradlew.bat" (
  echo Gradle wrapper is missing. Open this project in Android Studio once and use Gradle sync,
  echo or install Gradle and run: gradle wrapper --gradle-version 8.10.2
  exit /b 1
)
call "%~dp0gradlew.bat" :app:assembleDebug
if %errorlevel% neq 0 exit /b %errorlevel%
echo.
echo APK: %~dp0app\build\outputs\apk\debug\app-debug.apk
