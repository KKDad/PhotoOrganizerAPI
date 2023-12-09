# PhotoOrganizerAPI

[![Java CI with Gradle](https://github.com/KKDad/PhotoOrganizerAPI/actions/workflows/gradle.yml/badge.svg)](https://github.com/KKDad/PhotoOrganizerAPI/actions/workflows/gradle.yml)

Yet another photo organizer. To organize photos exported from social services, and enriched using Google Vision

Current services supported:
- Google, exports from takeout.google.com

Planned:
- Facebook

## Accessing the API
See [photo-server](photo-server/README.md) API documentation.

## Components

Built using Spring boot, Leverages the Google Visions APIs for photo classification.

## Running the application

To run PhotoOrganizer
~~~
./gradlew bootrun
~~~
Example output:
~~~
$ ./gradlew bootrun

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

2023-12-09T14:15:22.832-05:00  INFO 1571 --- [PhotoOrganizer] [           main] o.stapledon.PhotoOrganizerApplication    : Starting PhotoOrganizerApplication using Java 17.0.9 with PID 1571 (/Users/agilbert/git/PhotoOrganizerAPI/photo-server/build/classes/java/main started by agilbert in /Users/agilbert/git/PhotoOrganizerAPI/photo-server)
2023-12-09T14:15:22.833-05:00  INFO 1571 --- [PhotoOrganizer] [           main] o.stapledon.PhotoOrganizerApplication    : No active profile set, falling back to 1 default profile: "default"
2023-12-09T14:15:23.399-05:00  INFO 1571 --- [PhotoOrganizer] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2023-12-09T14:15:23.445-05:00  INFO 1571 --- [PhotoOrganizer] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 40 ms. Found 2 JPA repository interfaces.
2023-12-09T14:15:23.952-05:00  INFO 1571 --- [PhotoOrganizer] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2023-12-09T14:15:23.958-05:00  INFO 1571 --- [PhotoOrganizer] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2023-12-09T14:15:23.959-05:00  INFO 1571 --- [PhotoOrganizer] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.16]
2023-12-09T14:15:23.999-05:00  INFO 1571 --- [PhotoOrganizer] [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2023-12-09T14:15:24.000-05:00  INFO 1571 --- [PhotoOrganizer] [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1143 ms
2023-12-09T14:15:24.331-05:00  INFO 1571 --- [PhotoOrganizer] [           main] liquibase.database                       : Set default schema name to public
~~~

## Google API

This project needs a Service Key. Follow the guide here https://cloud.google.com/vision/docs/setup 
and download the service key json file. Eg: In the Cloud Console, click the email address for the 
service userInfo visionapio-453:
- Click Keys.
- Click Add key, then click Create new key.
- Click Create. A JSON key file is downloaded to your computer.
- Click Close.
~~~

## Docker

Docker image build using buildpacks
~~~
$ ./gradlew bootBuildImage
~~~

## Accessing the swagger UI

The swagger UI is available at http://localhost:8080/swagger-ui.html