#!/bin/bash

set -e

# shellcheck disable=SC2046
export $(grep -v '^#' .env | xargs)

./gradlew project:pay:bootJar

# shellcheck disable=SC2010
JAR_FILE=$(ls project/pay/build/libs/*.jar | grep -v 'original' | head -n 1)

if [[ -z "$JAR_FILE" ]]; then
  echo "JAR-файл не найден в project/pay/build/libs/."
  exit 1
fi

java -jar "$JAR_FILE" &

PAY_PID=$!

./gradlew project:intershop:bootJar

# shellcheck disable=SC2010
JAR_FILE=$(ls project/intershop/build/libs/*.jar | grep -v 'original' | head -n 1)

if [[ -z "$JAR_FILE" ]]; then
  echo "JAR-файл не найден в project/intershop/build/libs/."
  exit 1
fi

java -jar "$JAR_FILE" &


INTERSHOP_PID=$!

# shellcheck disable=SC2064
trap "kill $PAY_PID $INTERSHOP_PID" SIGINT SIGTERM

wait $PAY_PID $INTERSHOP_PID