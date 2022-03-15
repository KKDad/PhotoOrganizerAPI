package org.stapledon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.stapledon.service.VisionService;

import java.io.IOException;

@SpringBootApplication
public class PhotoOrganizerApplication {

	private static final Logger logger = LoggerFactory.getLogger(PhotoOrganizerApplication.class);


	public static void main(String[] args) throws IOException {
		var ctx = SpringApplication.run(PhotoOrganizerApplication.class, args);

//		var elastic = ctx.getBean("restHighLevelClient", RestHighLevelClient.class);
//		logger.info("Elastic: {}", elastic.cluster().health(new ClusterHealthRequest(), RequestOptions.DEFAULT).getClusterName());

		logger.info("Being Enrichment Scan");
		var vision = ctx.getBean("visionService", VisionService.class);
		vision.enrich();

		ctx.close();
	}

}
