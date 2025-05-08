#!/bin/bash

set -e

# shellcheck disable=SC2046
export $(grep -v '^#' .env | xargs)

./gradlew project:pay:test