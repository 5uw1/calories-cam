#!/bin/sh
# Xcode Cloud runs this automatically after cloning the repo, before the build.
# Echo statements throughout are deliberate: this script's log is the only way
# to see what happened here, and silent success looks identical to "never ran"
# without them.
set -e

echo "ci_post_clone: writing Secrets.xcconfig"
CONFIG_DIR="$CI_PRIMARY_REPOSITORY_PATH/iosApp/Configuration"
mkdir -p "$CONFIG_DIR"
echo "GEMINI_API_KEY = ${GEMINI_API_KEY}" > "$CONFIG_DIR/Secrets.xcconfig"

# Xcode Cloud images have no Android SDK. The "Compile Kotlin Framework" build
# phase runs Gradle, which must *configure* the whole :composeApp module —
# including its Android target — even though only the iOS framework task
# actually executes. An empty stub directory satisfies that check; no real
# Android SDK components are needed since no Android compile task runs here.
echo "ci_post_clone: writing stub local.properties for Gradle's Android target"
ANDROID_SDK_STUB="$HOME/android-sdk-stub"
mkdir -p "$ANDROID_SDK_STUB"
echo "sdk.dir=$ANDROID_SDK_STUB" > "$CI_PRIMARY_REPOSITORY_PATH/local.properties"

# Xcode Cloud images ship with no real JDK — but macOS still puts a stub
# /usr/bin/java on PATH that merely prints "Unable to locate a Java Runtime"
# when run, so `command -v java` (or a plain existence check) reports a false
# positive and can't be used to skip this step. This script only ever runs
# under Xcode Cloud, so just always install a real JDK unconditionally.
echo "ci_post_clone: installing portable JDK"
JDK_DIR="$HOME/.jdks/temurin-current"
mkdir -p "$JDK_DIR"
curl -fSL "https://api.adoptium.net/v3/binary/latest/21/ga/mac/aarch64/jdk/hotspot/normal/eclipse?project=jdk" \
  -o "$HOME/jdk.tar.gz"
tar -xzf "$HOME/jdk.tar.gz" -C "$JDK_DIR" --strip-components=1
rm -f "$HOME/jdk.tar.gz"
"$JDK_DIR/Contents/Home/bin/java" -version
echo "ci_post_clone: JDK ready at $JDK_DIR"
