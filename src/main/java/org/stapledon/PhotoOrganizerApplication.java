package org.stapledon;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.stapledon.components.PhotoService;
import org.stapledon.dto.Photo;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class PhotoOrganizerApplication {

	private static final Logger logger = LoggerFactory.getLogger(PhotoOrganizerApplication.class);


	public static void main(String[] args) throws IOException {
		var ctx = SpringApplication.run(PhotoOrganizerApplication.class, args);

		var elastic = ctx.getBean("restHighLevelClient", RestHighLevelClient.class);
		logger.info("Elastic: {}", elastic.cluster().health(new ClusterHealthRequest(), RequestOptions.DEFAULT).getClusterName());

		var photoService = ctx.getBean("photoService", PhotoService.class);

		List<Photo> results = photoService.load("R:/Photos/Moments/2013-12-13");
		logger.info("Loaded {} items", results.size());

		ctx.close();
	}

}
