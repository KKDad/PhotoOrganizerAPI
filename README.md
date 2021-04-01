# PhotoOrganizer
Yes another photo organizer. To organize photos exported from takeout.google.com

## Components

### API
API for interacting with the Photo Organizer

### Engine
Engine that performs jobs and reports status

### Common
Utility functions and code shared between the API and the Engine
- Database Daos (Managed by LiquidBase)
- Models (Annotated for both Json serialization and Construction Mappers)

# Development

This project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## Running the application

To run the API
~~~
./gradlew shadowJar
java -jar photo-api/build/libs/photo-api-1.0.0-alpha-all.jar server config.yml
~~~
Example output:
~~~
$ java -jar photo-api/build/libs/photo-api-1.0.0-alpha-all.jar server config.yml
INFO  [2021-04-01 12:44:34,480] io.dropwizard.server.DefaultServerFactory: Registering jersey handler with root path prefix: /
INFO  [2021-04-01 12:44:34,484] io.dropwizard.server.DefaultServerFactory: Registering admin handler with root path prefix: /
INFO  [2021-04-01 12:44:35,530] io.dropwizard.server.ServerFactory: Starting KnockKnock
  _________ __                .__             .___
 /   _____//  |______  ______ |  |   ____   __| _/____   ____
 \_____  \\   __\__  \ \____ \|  | _/ __ \ / __ |/  _ \ /    \
 /        \|  |  / __ \|  |_> >  |_\  ___// /_/ (  <_> )   |  \
/_______  /|__| (____  /   __/|____/\___  >____ |\____/|___|  /
        \/           \/|__|             \/     \/           \/
INFO  [2021-04-01 12:44:35,626] org.eclipse.jetty.setuid.SetUIDListener: Opened application@70cccd8f{HTTP/1.1, (http/1.1)}{0.0.0.0:8080}
INFO  [2021-04-01 12:44:35,626] org.eclipse.jetty.setuid.SetUIDListener: Opened admin@5f172d4a{HTTP/1.1, (http/1.1)}{0.0.0.0:8081}
INFO  [2021-04-01 12:44:35,628] org.eclipse.jetty.server.Server: jetty-9.4.30.v20200611; built: 2020-06-11T12:34:51.929Z; git: 271836e4c1f4612f12b7bb13ef5a92a927634b0d; jvm 13.0.1+9
INFO  [2021-04-01 12:44:35,636] org.stapledon.photo.es.ManagedEsClient: Starting Elasticsearch client...
INFO  [2021-04-01 12:44:36,174] io.dropwizard.jersey.DropwizardResourceConfig: The following paths were found for the configured resources:

    GET     /media/photo (org.stapledon.photo.api.ImageProcessor)
    GET     /media/search (org.stapledon.photo.api.ImageProcessor)

INFO  [2021-04-01 12:44:36,178] org.eclipse.jetty.server.handler.ContextHandler: Started i.d.j.MutableServletContextHandler@4342c13{/,null,AVAILABLE}
INFO  [2021-04-01 12:44:36,181] io.dropwizard.setup.AdminEnvironment: tasks =

    POST    /tasks/log-level (io.dropwizard.servlets.tasks.LogConfigurationTask)
    POST    /tasks/gc (io.dropwizard.servlets.tasks.GarbageCollectionTask)

INFO  [2021-04-01 12:44:36,210] org.eclipse.jetty.server.handler.ContextHandler: Started i.d.j.MutableServletContextHandler@11381415{/,null,AVAILABLE}
INFO  [2021-04-01 12:44:36,219] org.eclipse.jetty.server.AbstractConnector: Started application@70cccd8f{HTTP/1.1, (http/1.1)}{0.0.0.0:8080}
INFO  [2021-04-01 12:44:36,224] org.eclipse.jetty.server.AbstractConnector: Started admin@5f172d4a{HTTP/1.1, (http/1.1)}{0.0.0.0:8081}
INFO  [2021-04-01 12:44:36,225] org.eclipse.jetty.server.Server: Started @3498ms
~~~
