package org.stapledon.photo.main;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stapledon.photo.api.ImageProcessor;
import org.stapledon.photo.configuration.PhotoAPIConfiguration;

public class PhotoCatalogerAPI extends Application<PhotoAPIConfiguration> {
    private static final Logger logger = LoggerFactory.getLogger(PhotoCatalogerAPI.class);

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
        //logger.info("PhotoCataloger Serving {} resources from {}", configuration.getResources().size(), configuration.getDataDirectory());

        final NoOpHealthCheck healthCheck = new NoOpHealthCheck();
        environment.healthChecks().register("template", healthCheck);

        final ImageProcessor imageProcessor = new ImageProcessor();
        environment.jersey().register(imageProcessor);

        //final ResourcesAPI resource = new ResourcesAPI(configuration.getResources());
        //environment.jersey().register(resource);
    }


}