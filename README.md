# PhotoOrganizer
Yet another photo organizer. To organize photos exported from takeout.google.com

## Components

Built using Spring boot, Leverages the Google Visions APIs for photo classification.

# Development

This project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
 :: Spring Boot ::                (v2.6.4)

2022-03-07 19:14:44.663  INFO 8852 --- [           main] org.stapledon.PhotoOrganizerApplication  : Starting PhotoOrganizerApplication using Java 13 on VDI-w10-Development with PID 8852 (C:\git\PhotoOrganizer\build\classes\java\main started by Adrian in C:\git\PhotoOrganizer)
2022-03-07 19:14:44.668  INFO 8852 --- [           main] org.stapledon.PhotoOrganizerApplication  : No active profile set, falling back to 1 default profile: "default"
2022-03-07 19:14:45.176  INFO 8852 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data Elasticsearch repositories in DEFAULT mode.
2022-03-07 19:14:45.187  INFO 8852 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 6 ms. Found 0 Elasticsearch repository interfaces.
2022-03-07 19:14:45.190  INFO 8852 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data Reactive Elasticsearch repositories in DEFAULT mode.
2022-03-07 19:14:45.193  INFO 8852 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 2 ms. Found 0 Reactive Elasticsearch repository interfaces.
2022-03-07 19:14:48.385  INFO 8852 --- [           main] o.s.b.web.embedded.netty.NettyWebServer  : Netty started on port 8080
2022-03-07 19:14:48.394  INFO 8852 --- [           main] org.stapledon.PhotoOrganizerApplication  : Started PhotoOrganizerApplication in 4.228 seconds (JVM running for 4.7)
2022-03-07 19:14:48.395  INFO 8852 --- [           main] org.stapledon.components.PhotoService    : Loading all photos under: R:/Photos/Moments/2013-12-13
2022-03-07 19:14:48.503  INFO 8852 --- [           main] org.stapledon.PhotoOrganizerApplication  : Loaded 2 items

## Google API

This project needs a Service Key. Follow the guide here https://cloud.google.com/vision/docs/setup 
and download the service key json file. Eg: In the Cloud Console, click the email address for the 
service account visionapio-453:
- Click Keys.
- Click Add key, then click Create new key.
- Click Create. A JSON key file is downloaded to your computer.
- Click Close.
~~~
