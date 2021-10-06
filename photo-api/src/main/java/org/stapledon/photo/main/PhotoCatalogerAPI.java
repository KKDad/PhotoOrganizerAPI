package org.stapledon.photo.main;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.stapledon.photo.api.ImageProcessor;
import org.stapledon.photo.configuration.PhotoAPIConfiguration;
import org.stapledon.photo.es.ManagedEsClient;
import org.stapledon.photo.service.ElasticService;

public class PhotoCatalogerAPI extends Application<PhotoAPIConfiguration> {

    public static void main(String[] args) throws Exception {
        new PhotoCatalogerAPI().run(args);
    }

    @Override
    public String getName() {
        return "KnockKnock";
    }


    @Override
    public void run(PhotoAPIConfiguration configuration, Environment environment)
    {
        final var imageProcessor = new ImageProcessor();
        environment.jersey().register(imageProcessor);

        final var managedClient = new ManagedEsClient(configuration.elasticsearchConfig);
        environment.lifecycle().manage(managedClient);
        ElasticService.use(managedClient);

        final var healthCheck = new NoOpHealthCheck();
        environment.healthChecks().register("template", healthCheck);
        environment.healthChecks().register("ES cluster health", new EsClusterHealthCheck(managedClient.getClient()));
    }


}