package org.stapledon.photo.service;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stapledon.photo.dto.Photo;
import org.stapledon.photo.dto.PhotoES;
import org.stapledon.photo.es.ManagedEsClient;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ElasticService {
    private static final Logger logger = LoggerFactory.getLogger(ElasticService.class);
    private static ManagedEsClient esClient = null;

    public static void use(ManagedEsClient client) {
        esClient = client;
    }

    public boolean indexDocument(Photo photo, String indexPattern) {
        try {
            String indexName = getIndexName(indexPattern, photo);

            var request = new IndexRequest(indexName);
            PhotoES indexDocument = new PhotoService().convert(photo);
            request.source(indexDocument.toJson(), XContentType.JSON);

            var indexResponse = esClient.getClient().index(request, RequestOptions.DEFAULT);
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

    public boolean indexDirectory(String directory, String indexPattern, int maxDocs) {
        var success = true;
        var count = 0;
        var photoService = new PhotoService();
        List<Photo> photos = photoService.load(directory, maxDocs);
        if (maxDocs < photos.size())
            photos = photos.subList(0, maxDocs);
        try {
            for (Photo p : photos) {
                String indexName = getIndexName(indexPattern, p);

                var request = new IndexRequest(indexName);
                PhotoES indexDocument = photoService.convert(p);
                request.source(indexDocument.toJson(), XContentType.JSON);

                var indexResponse = esClient.getClient().index(request, RequestOptions.DEFAULT);
                if (indexResponse.getResult() != DocWriteResponse.Result.CREATED && indexResponse.getResult() != DocWriteResponse.Result.UPDATED)
                    success = false;
                else
                    count++;
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
        logger.info("Processed {} photos {}", count, success ? "successfully" : "errors");
        return success;
    }

    private String getIndexName(String indexPattern, Photo p) {
        var patternFormatter = DateTimeFormatter.ofPattern(indexPattern);
        return patternFormatter.format(p.getLocalDate());
    }
}
