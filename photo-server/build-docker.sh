#!/bin/sh
#
# Build photo-organizer-api and push to the Stapledon docker registry

set -e

DOCKER_REGISTRY=registry.stapledon.ca
BUILD_TAG=$1
if [ -z "${BUILD_TAG}" ]; then
   echo "Build Tag is required"

   # Get a sorted list of the current tags in the respository
   echo "Current Tags for kkdad/photo-organizer-api:"
   curl -s "https://${DOCKER_REGISTRY}/v2/kkdad/photo-organizer-api/tags/list" | jq -r '.tags[]' | sort -r
   exit
fi

# Clean and build the jar in a subshell
( cd .. &&  ./gradlew clean bootJar )

# Build and push with the specified version
docker build -f Dockerfile . --tag kkdad/photo-organizer-api:"${BUILD_TAG}" --platform linux/amd64
docker tag kkdad/photo-organizer-api:"${BUILD_TAG}" $DOCKER_REGISTRY/kkdad/photo-organizer-api:"${BUILD_TAG}"
docker push $DOCKER_REGISTRY/kkdad/photo-organizer-api:"${BUILD_TAG}"

# Tag as latest
docker tag kkdad/photo-organizer-api:"${BUILD_TAG}" $DOCKER_REGISTRY/kkdad/photo-organizer-api:latest
docker push $DOCKER_REGISTRY/kkdad/photo-organizer-api:latest

