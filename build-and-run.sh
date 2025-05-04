#!/usr/bin/env bash

echo "Building the project services..."
pushd producer-service
./gradlew clean build -Dquarkus.package.type=fast-jar
popd

echo "Building the consumer service..."
pushd consumer-service
./gradlew clean build -Dquarkus.package.type=fast-jar
popd

echo "Building the Docker images..."
docker-compose up --build
