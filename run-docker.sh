#!/bin/bash

./gradlew bootJar

docker build -t intershop-app:latest .

docker run -p 8080:8080 -v upload:/intershop/upload intershop-app:latest