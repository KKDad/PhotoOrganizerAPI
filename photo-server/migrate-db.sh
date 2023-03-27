#!/bin/sh
#
# Using docker, drop and recreate the database used by photo-organizer-api

set +e
DATASOURCE_PASSWORD=$1
if [ -z DATASOURCE_PASSWORD ]; then
   echo "DATASOURCE_PASSWORD is required"
   exit
fi
DATASOURCE_HOST=photo-api-postgres
DATASOURCE_PORT=5432
DATASOURCE_USERNAME=postgres

SCHEMA_TO_CREATE=photo_organizer
DOCKER_REGISTRY=registry.stapledon.ca

# Drop the schema
docker run --rm \
    --network stapledon-network \
    -e "PGPASSWORD=$DATASOURCE_PASSWORD" \
    postgres:13 \
    psql -h $DATASOURCE_HOST -p $DATASOURCE_PORT -U $DATASOURCE_USERNAME -d postgres -c "DROP DATABASE IF EXISTS $SCHEMA_TO_CREATE WITH (FORCE)"

# Create the schema
docker run --rm \
    --network stapledon-network \
    -e "PGPASSWORD=$DATASOURCE_PASSWORD" \
    postgres:13 \
    psql -h $DATASOURCE_HOST -p $DATASOURCE_PORT -U $DATASOURCE_USERNAME -d postgres -c "CREATE DATABASE $SCHEMA_TO_CREATE"

# Run the migrations
docker run --pull=always --network=stapledon-network \
           -e SPRING_DATASOURCE_URL=jdbc:postgresql://$DATASOURCE_HOST:$DATASOURCE_PORT/$SCHEMA_TO_CREATE \
           -e SPRING_DATASOURCE_PASSWORD=$DATASOURCE_PASSWORD \
           -e SPRING_DATASOURCE_USERNAME=$DATASOURCE_USERNAME \
           -e SPRING_MAIN_WEB-APPLICATION-TYPE=none \
           -d $DOCKER_REGISTRY:5000/kkdad/photo-organizer-api:latest --db-setup

# After the migrations are done, clean up the container
docker ps -a | grep "photo-organizer-api" | awk '{print $1}' | xargs docker rm
