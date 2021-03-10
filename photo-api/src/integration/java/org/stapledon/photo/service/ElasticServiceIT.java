package org.stapledon.photo.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stapledon.photo.configuration.EsConfiguration;
import org.stapledon.photo.configuration.PhotoAPIConfiguration;
import org.stapledon.photo.dto.Photo;
import org.stapledon.photo.es.ManagedEsClient;

import java.io.File;

class ElasticServiceIT
{
    private ElasticService subject;
    private static ManagedEsClient managedClient;
    private static final Logger logger = LoggerFactory.getLogger(ElasticServiceIT.class);

    private String TEST_INDEX_NAME = "photos_test";


    @BeforeEach
    public void setUp()
    {
        PhotoAPIConfiguration configuration = new PhotoAPIConfiguration();
        configuration.setEsConfiguration(new EsConfiguration());
        configuration.getEsConfiguration().setClusterName("elasticsearch");
        configuration.getEsConfiguration().getServers().add("127.0.0.1:9200");
        managedClient = new ManagedEsClient(configuration.getEsConfiguration());
        ElasticService.use(managedClient);

        subject = new ElasticService();
    }

//    @AfterAll
//    public void tearDown()
//    {
//        try {
//            // Clean up
//            RestHighLevelClient client = managedClient.getClient();
//            DeleteIndexRequest request = new DeleteIndexRequest(TEST_INDEX_NAME);
//            client.indices().delete(request, RequestOptions.DEFAULT);
//            AcknowledgedResponse deleteIndexResponse = client.indices().delete(request, RequestOptions.DEFAULT);
//        } catch (ElasticsearchException | IOException exception)  {
//            // No Op, test may have failed, just log a message and move on.
//            logger.info(exception.getLocalizedMessage());
//        }
//    }

    @Test
    public void indexDocumentTest()
    {
        // Arrange
        ClassLoader classLoader = this.getClass().getClassLoader();
        String path = new File(classLoader.getResource("FB_IMG_13869672665371829.jpg.json").getPath()).getParent();
        Photo photo = new PhotoService().loadPhoto(path + "/FB_IMG_13869672665371829.jpg");

        // Act
        Boolean result = subject.IndexDocument(photo,TEST_INDEX_NAME);

        Assertions.assertTrue(result);
    }

    @Test
    public void indexDirectoryTest()
    {
        // Act
        Boolean result = subject.IndexDirectory("R:/Drive/Moments",TEST_INDEX_NAME);

        Assertions.assertTrue(result);
    }

}