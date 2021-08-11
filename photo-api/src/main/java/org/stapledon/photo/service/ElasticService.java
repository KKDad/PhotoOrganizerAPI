package org.stapledon.photo.service;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stapledon.photo.dto.Photo;
import org.stapledon.photo.dto.PhotoES;
import org.stapledon.photo.es.ManagedEsClient;

import java.io.IOException;
import java.util.List;

public class ElasticService {
    private static final Logger logger = LoggerFactory.getLogger(ElasticService.class);
    private static ManagedEsClient esClient = null;

    public static void use(ManagedEsClient client) {
        esClient = client;
    }

    public boolean indexDocument(Photo photo, String index) {
        try {
            IndexRequest request = new IndexRequest(index);
            PhotoES indexDocument = new PhotoService().convert(photo);
            request.source(indexDocument.toJson(), XContentType.JSON);

            IndexResponse indexResponse = esClient.getClient().index(request, RequestOptions.DEFAULT);
            return (indexResponse.getResult() == DocWriteResponse.Result.CREATED ||
                    indexResponse.getResult() == DocWriteResponse.Result.UPDATED);

        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
        return false;
    }

    public boolean indexDirectory(String directory, String index) {
        return indexDirectory(directory, index, Integer.MAX_VALUE);
    }

    public boolean indexDirectory(String directory, String index, int maxDocs) {
        boolean success = true;
        int count = 0;
        PhotoService photoService = new PhotoService();
        List<Photo> photos = photoService.load(directory, maxDocs);
        if (maxDocs < photos.size())
            photos = photos.subList(0, maxDocs);
        try {
            for (Photo p : photos) {
                IndexRequest request = new IndexRequest(index);
                PhotoES indexDocument = photoService.convert(p);
                request.source(indexDocument.toJson(), XContentType.JSON);

                IndexResponse indexResponse = esClient.getClient().index(request, RequestOptions.DEFAULT);
                if (indexResponse.getResult() != DocWriteResponse.Result.CREATED && indexResponse.getResult() != DocWriteResponse.Result.UPDATED)
                    success = false;
                else
                    count++;
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
        logger.info("Processed {} photos", count);
        return success;
    }
}
