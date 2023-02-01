package org.stapledon.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.stapledon.GoogleCredentialConfig;

import java.io.IOException;
import java.nio.file.Path;

@SpringBootTest
@Import(GoogleCredentialConfig.class)
class MetadataToolIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(MetadataToolIntegrationTest.class);


    @Autowired
    private MetadataTool metadataTool;

    @Test
    void loadDirectory() throws IOException {
        var classPathResource = new ClassPathResource("2019-11-13");
        var testResources = Path.of(classPathResource.getURI());

        var results = metadataTool.fetchAll(testResources);
        Assertions.assertEquals(5, results.size());
        LOG.info("Loaded {} items", results.size());
    }
}