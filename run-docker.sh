#!/bin/bash

./gradlew project:intershop:bootJar
./gradlew project:pay:bootJar

docker-compose up --build