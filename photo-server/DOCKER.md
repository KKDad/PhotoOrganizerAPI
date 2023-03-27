To build and launch the PhotoOrganizer API, build the docker container and push the image to the local registry using
the `build-docker.sh` script.

PhotoOrganizer API requires a running postgresql instance:
~~~
docker run --network=stapledon-network --name photo-api-postgres -e POSTGRES_PASSWORD=mysecretpassword -d postgres:13
~~~
And then run the PhotoOrganizer API docker image:
~~~
docker stop photo-api
docker rm photo-api
docker run --network=stapledon-network --name photo-api -e SPRING_DATASOURCE_URL=jdbc:postgresql://photo-api-postgres:5432/photo_organizer -e SPRING_DATASOURCE_PASSWORD=mysecretpassword -e SPRING_DATASOURCE_USERNAME=postgres -d kkdad/photo-organizer-api:0.0.3
~~~



Quickstart: Using `colima` on the Mac M2
~~~
 brew install colima
 colima start
 colima status
 mkdir /Users/agilbert/.docker/run
 ln -s /Users/agilbert/.colima/default/docker.sock /Users/agilbert/.docker/run/docker.sock
~~~

Adapted from
https://alexos.dev/2022/01/02/docker-desktop-alternatives-for-m1-mac/


