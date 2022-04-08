# PhotoOrganizer
Yet another photo organizer. To organize photos exported from social services, and enriched using Google Vision

Current services supported:
- Google, exports from takeout.google.com

Planned:
- Facebook

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
 :: Spring Boot ::                (v2.6.4)

2022-03-23 20:07:42.987  INFO 7772 --- [           main] org.stapledon.PhotoOrganizerApplication  : Starting PhotoOrganizerApplication using Java 13 on VDI-w10-Development with PID 7772 (C:\git\PhotoOrganizer\build\classes\java\main started by Adrian in C:\git\PhotoOrganizer)
2022-03-23 20:07:42.989  INFO 7772 --- [           main] org.stapledon.PhotoOrganizerApplication  : No active profile set, falling back to 1 default profile: "default"
2022-03-23 20:07:43.640  INFO 7772 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data Elasticsearch repositories in DEFAULT mode.
2022-03-23 20:07:43.650  INFO 7772 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 6 ms. Found 0 Elasticsearch repository interfaces.
2022-03-23 20:07:43.654  INFO 7772 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data Reactive Elasticsearch repositories in DEFAULT mode.
2022-03-23 20:07:43.657  INFO 7772 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 3 ms. Found 0 Reactive Elasticsearch repository interfaces.
2022-03-23 20:07:44.099  INFO 7772 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2022-03-23 20:07:44.108  INFO 7772 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2022-03-23 20:07:44.108  INFO 7772 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.58]
2022-03-23 20:07:44.210  INFO 7772 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2022-03-23 20:07:44.211  INFO 7772 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1187 ms
2022-03-23 20:07:44.408  INFO 7772 --- [           main] o.s.c.g.core.DefaultCredentialsProvider  : Default credentials provider for service account visionapio-453@speedy-emissary-344223.iam.gserviceaccount.com
2022-03-23 20:07:45.566  INFO 7772 --- [           main] o.s.c.g.a.c.GcpContextAutoConfiguration  : The default project ID is speedy-emissary-344223
2022-03-23 20:07:45.569  INFO 7772 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 3 endpoint(s) beneath base path '/actuator'
~~~

## Google API

This project needs a Service Key. Follow the guide here https://cloud.google.com/vision/docs/setup 
and download the service key json file. Eg: In the Cloud Console, click the email address for the 
service account visionapio-453:
- Click Keys.
- Click Add key, then click Create new key.
- Click Create. A JSON key file is downloaded to your computer.
- Click Close.
~~~
