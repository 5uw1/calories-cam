#!/bin/sh
# Xcode Cloud runs this automatically after cloning the repo, before the build.
# Secrets.xcconfig is gitignored (never committed), so it doesn't exist in a
# fresh Xcode Cloud checkout. Recreate it here from a Secret Environment
# Variable named GEMINI_API_KEY, configured on the Xcode Cloud workflow in
# App Store Connect (Xcode Cloud > your workflow > Environment Variables).
set -e

CONFIG_DIR="$CI_PRIMARY_REPOSITORY_PATH/iosApp/Configuration"
mkdir -p "$CONFIG_DIR"
echo "GEMINI_API_KEY = ${GEMINI_API_KEY}" > "$CONFIG_DIR/Secrets.xcconfig"

# Xcode Cloud images ship with no Java runtime at all, so ./gradlew (invoked by
# the "Compile Kotlin Framework" build phase) fails immediately with
# "Unable to locate a Java Runtime." Download a portable JDK once to a fixed
# path; that build phase's script checks this same path and prepends it to
# PATH when `java` isn't already available (see project.pbxproj).
if ! command -v java >/dev/null 2>&1; then
  JDK_DIR="$HOME/.jdks/temurin-current"
  mkdir -p "$JDK_DIR"
  curl -fsSL "https://api.adoptium.net/v3/binary/latest/21/ga/mac/aarch64/jdk/hotspot/normal/eclipse?project=jdk" \
    -o "$HOME/jdk.tar.gz"
  tar -xzf "$HOME/jdk.tar.gz" -C "$JDK_DIR" --strip-components=1
  rm -f "$HOME/jdk.tar.gz"
fi

# Xcode Cloud images have no Android SDK. The "Compile Kotlin Framework" build
# phase runs Gradle, which must *configure* the whole :composeApp module —
# including its Android target — even though only the iOS framework task
# actually executes. An empty stub directory satisfies that check; no real
# Android SDK components are needed since no Android compile task runs here.
ANDROID_SDK_STUB="$HOME/android-sdk-stub"
mkdir -p "$ANDROID_SDK_STUB"
echo "sdk.dir=$ANDROID_SDK_STUB" > "$CI_PRIMARY_REPOSITORY_PATH/local.properties"
