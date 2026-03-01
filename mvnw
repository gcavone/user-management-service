#!/bin/sh
# Maven Wrapper Script
set -e

MAVEN_WRAPPER_PROPERTIES=".mvn/wrapper/maven-wrapper.properties"
MAVEN_USER_HOME="${MAVEN_USER_HOME:-$HOME/.m2}"
MAVEN_WRAPPER_DIR="$MAVEN_USER_HOME/wrapper"

# Read distributionUrl from properties
DISTRIBUTION_URL=$(grep distributionUrl "$MAVEN_WRAPPER_PROPERTIES" | cut -d= -f2-)
MAVEN_VERSION=$(echo "$DISTRIBUTION_URL" | sed 's/.*apache-maven-\([0-9.]*\)-.*/\1/')
MAVEN_HOME="$MAVEN_WRAPPER_DIR/dists/apache-maven-$MAVEN_VERSION"

if [ ! -d "$MAVEN_HOME" ]; then
    echo "Downloading Maven $MAVEN_VERSION..."
    mkdir -p "$MAVEN_HOME"
    curl -sL "$DISTRIBUTION_URL" -o "/tmp/maven.zip"
    unzip -q "/tmp/maven.zip" -d "$MAVEN_HOME"
    rm "/tmp/maven.zip"
fi

MAVEN_BIN=$(find "$MAVEN_HOME" -name "mvn" -type f | head -1)
exec "$MAVEN_BIN" "$@"
