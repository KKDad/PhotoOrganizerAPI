package org.stapledon.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.stapledon.dto.Photo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest
class PhotoServiceIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(PhotoServiceIntegrationTest.class);


    @Autowired
    private PhotoService photoService;

    @Test
    void loadPhoto() throws IOException, URISyntaxException {

    }

    @Test
    void loadDirectory() throws IOException, URISyntaxException {
        var classPathResource = new ClassPathResource("2019-11-13");
        var testResources = Path.of(classPathResource.getURI());

        var results = photoService.scan(testResources);
        Assertions.assertEquals(5, results.size());
        LOG.info("Loaded {} items", results.size());    }
}