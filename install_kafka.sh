#!/usr/bin/env bash

set -euox pipefail

KAFKA_VERSION="4.2.0"
SCALA_VERSION="2.13"
ARCHIVE="kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz"
FOLDER="kafka_${SCALA_VERSION}-${KAFKA_VERSION}"
URL="https://downloads.apache.org/kafka/${KAFKA_VERSION}/${ARCHIVE}"

INSTALL_DIR="$(pwd)/kafka"


if [ -d "$INSTALL_DIR" ]; then
    rm -rf "$INSTALL_DIR"
fi

wget -O "$ARCHIVE" "$URL"
tar -xzf "$ARCHIVE"

mv "$FOLDER" "$INSTALL_DIR"

rm -f "$ARCHIVE"
