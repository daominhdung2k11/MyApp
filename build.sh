#!/bin/bash

# Build script for TrollApp on Termux

echo "Building TrollApp APK..."

# Create fake SDK structure for build
mkdir -p ~/android-sdk/{platforms,build-tools,licenses}

# Accept licenses
touch ~/android-sdk/licenses/android-sdk-license
echo -e "\n" > ~/android-sdk/licenses/android-sdk-license

# Set SDK path
export ANDROID_SDK_ROOT=~/android-sdk
export ANDROID_HOME=~/android-sdk

# Try to build without full SDK (using gradle wrappers)
cd /storage/emulated/0/TrollApp

# Clean previous builds
rm -rf build/ .gradle/

# Build APK
gradle assembleDebug --no-daemon --warning-mode=all 2>&1

echo ""
echo "Build completed!"
ls -lh app/build/outputs/apk/debug/ 2>/dev/null || echo "APK not found"
