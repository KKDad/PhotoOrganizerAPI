package org.stapledon.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

@SpringBootTest
class PhotoMetadataComponentIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(PhotoMetadataComponentIntegrationTest.class);


    @Autowired
    private PhotoMetadataComponent photoMetadataComponent;

    @Test
    void loadDirectory() throws IOException {
        var classPathResource = new ClassPathResource("2019-11-13");
        var testResources = Path.of(classPathResource.getURI());

        var results = photoMetadataComponent.fetchAll(testResources);
        Assertions.assertEquals(5, results.size());
        LOG.info("Loaded {} items", results.size());    }
}