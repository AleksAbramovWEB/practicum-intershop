#!/bin/sh

# shellcheck disable=SC2164
cd /intershop/

# shellcheck disable=SC2046
export $(grep -v '^#' .env | xargs)

java -jar app.jar