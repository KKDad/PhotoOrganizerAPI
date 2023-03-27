#!/bin/sh
#
# Build photo-organizer-api and push to the stapledon docker registry

set -e

BUILD_TAG=$1
if [ -z $BUILD_TAG ]; then
   echo "Build Tag is required"
   exit
fi

DOCKER_REGISTRY=registry.stapledon.ca

# Build and push with the specified version
docker build -f Dockerfile . --tag kkdad/photo-organizer-api:$BUILD_TAG --platform linux/amd64
docker tag kkdad/photo-organizer-api:$BUILD_TAG $DOCKER_REGISTRY:5000/kkdad/photo-organizer-api:$BUILD_TAG
docker push $DOCKER_REGISTRY:5000/kkdad/photo-organizer-api:$BUILD_TAG

# Tag as latest
docker tag kkdad/photo-organizer-api:$BUILD_TAG $DOCKER_REGISTRY:5000/kkdad/photo-organizer-api:latest
docker push $DOCKER_REGISTRY:5000/kkdad/photo-organizer-api:latest