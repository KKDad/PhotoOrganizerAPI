#!/bin/sh
#
# Using docker, launch the photo-organizer-api

set +e
#set -x
DATASOURCE_PASSWORD=$1
if [ -z DATASOURCE_PASSWORD ]; then
   echo "DATASOURCE_PASSWORD is required"
   exit
fi
DATASOURCE_HOST=photoDb13
DATASOURCE_PORT=5432
DATASOURCE_USERNAME=postgres

DATABASE=photos
DOCKER_REGISTRY=registry.stapledon.ca

# stop any running instances
echo "Stopping any running instances"
docker ps -a | grep "photo-organizer-api" | awk '{print $1}' | xargs docker stop
docker ps -a | grep "photo-organizer-api" | awk '{print $1}' | xargs docker rm

# Run the api
echo "Running the api"
docker run --pull=always --network=stapledon-network --name photo-organizer-api \
           -e SPRING_DATASOURCE_URL=jdbc:postgresql://$DATASOURCE_HOST:$DATASOURCE_PORT/$DATABASE \
           -e SPRING_DATASOURCE_PASSWORD=$DATASOURCE_PASSWORD \
           -e SPRING_DATASOURCE_USERNAME=$DATASOURCE_USERNAME \
           -p 9111:8080 \
           -d $DOCKER_REGISTRY/kkdad/photo-organizer-api:latest

