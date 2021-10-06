package org.stapledon.photo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stapledon.photo.configuration.PhotoAPIConfiguration;
import org.stapledon.photo.dto.Photo;
import org.stapledon.photo.es.ManagedEsClient;
import org.stapledon.photo.utils.ResourceHelper;

import java.io.File;

class ElasticServiceIT
{
    private ElasticService subject;
    private static final Logger logger = LoggerFactory.getLogger(ElasticServiceIT.class);

    private final String TEST_INDEX_NAME = "'photos_'yyyy";


    @BeforeEach
    public void setUp() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        PhotoAPIConfiguration configuration = mapper.readValue(ResourceHelper.getContents("test.yml"), PhotoAPIConfiguration.class);

        ManagedEsClient managedClient = new ManagedEsClient(configuration.elasticsearchConfig);
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
    void indexDocumentTest()
    {
        // Arrange
        ClassLoader classLoader = this.getClass().getClassLoader();
        String path = new File(classLoader.getResource("FB_IMG_13869672665371829.jpg.json").getPath()).getParent();
        Photo photo = new PhotoService(false).loadPhoto(path + "/FB_IMG_13869672665371829.jpg");

        // Act
        boolean result = subject.indexDocument(photo,TEST_INDEX_NAME);

        Assertions.assertTrue(result);
    }

    @Test
    void indexDirectoryTest()
    {
        // Act
        long startTime = System.currentTimeMillis();
        boolean result = subject.indexDirectory("R:/Drive/Moments",TEST_INDEX_NAME);
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
        Assertions.assertTrue(result);
    }

}