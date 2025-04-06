#!/bin/bash

set -e

# shellcheck disable=SC2046
export $(grep -v '^#' .env | xargs)

./gradlew bootJar

# shellcheck disable=SC2010
JAR_FILE=$(ls build/libs/*.jar | grep -v 'original' | head -n 1)

if [[ -z "$JAR_FILE" ]]; then
  echo "JAR-файл не найден в build/libs/."
  exit 1
fi

java -jar "$JAR_FILE"
