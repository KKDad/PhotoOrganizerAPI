#!/bin/sh
#
# Using docker, drop and recreate the database used by photo-organizer-api

set +e
#set -x
DATASOURCE_PASSWORD=$1
if [ -z "${DATASOURCE_PASSWORD}" ]; then
   echo "DATASOURCE_PASSWORD is required"
   exit
fi
# If the second argument is "--recreate", then drop and recreate the database
RECREATE=$2
DATASOURCE_HOST=photoDb13
DATASOURCE_PORT=5432
DATASOURCE_USERNAME=postgres

SCHEMA_TO_CREATE=photos
DOCKER_REGISTRY=registry.stapledon.ca

if [ "$RECREATE" = "--recreate" ]; then
    echo "Recreating the database"
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
else
    echo "Not recreating the database"
    exit
fi

# Run the migrations
docker run --pull=always --network=stapledon-network \
           -e SPRING_DATASOURCE_URL=jdbc:postgresql://$DATASOURCE_HOST:$DATASOURCE_PORT/$SCHEMA_TO_CREATE \
           -e SPRING_DATASOURCE_PASSWORD=$DATASOURCE_PASSWORD \
           -e SPRING_DATASOURCE_USERNAME=$DATASOURCE_USERNAME \
           -e SPRING_MAIN_WEB-APPLICATION-TYPE=none \
           -d $DOCKER_REGISTRY/kkdad/photo-organizer-api:latest --db-setup

# After the migrations are done, clean up the container that ran
#docker ps -a | grep "photo-organizer-api" | awk '{print $1}' | xargs docker rm
