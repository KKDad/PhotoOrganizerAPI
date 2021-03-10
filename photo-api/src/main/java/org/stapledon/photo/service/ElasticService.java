package org.stapledon.photo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stapledon.photo.dto.Photo;
import org.stapledon.photo.dto.PhotoES;
import org.stapledon.photo.es.ManagedEsClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ElasticService
{
    private static final Logger logger = LoggerFactory.getLogger(ElasticService.class);
    private static ManagedEsClient esClient = null;

    public static void use(ManagedEsClient client) { esClient = client; }

    private List<Integer> successCodes = Arrays.asList(0, 1);

    public boolean IndexDocument(Photo photo, String index)
    {
        try {
            IndexRequest request = new IndexRequest(index);
            PhotoES indexDocument = new PhotoService().convert(photo);
            request.source(indexDocument.toJson(), XContentType.JSON);

            IndexResponse indexResponse = esClient.getClient().index(request, RequestOptions.DEFAULT);
            return indexResponse.getResult().ordinal() == 1;

        } catch (IOException e ) {
            logger.error(e.getLocalizedMessage());
        }
        return false;
    }

    public boolean IndexDirectory(String directory, String index)
    {
        boolean success = true;
        PhotoService photoService = new PhotoService();
        List<Photo> photos = photoService.load(directory);
        try {
            for (Photo p : photos) {
                IndexRequest request = new IndexRequest(index);
                PhotoES indexDocument = photoService.convert(p);
                request.source(indexDocument.toJson(), XContentType.JSON);

                IndexResponse indexResponse = esClient.getClient().index(request, RequestOptions.DEFAULT);
                if (!(successCodes.contains(indexResponse.getResult().ordinal()))) {
                    success = false;
                }
            }
        } catch (IOException e ) {
            logger.error(e.getLocalizedMessage());
        }
        return success;
    }
}
