package org.stapledon;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.stapledon.service.TidyUpService;

import java.io.IOException;

@SpringBootApplication
@Slf4j
public class TakeoutOrganizerApplication {

    public static void main(String[] args) throws IOException {
        var ctx = SpringApplication.run(TakeoutOrganizerApplication.class, args);

		var elastic = ctx.getBean("restHighLevelClient", RestHighLevelClient.class);
		var health = elastic.cluster().health(new ClusterHealthRequest(), RequestOptions.DEFAULT);
		log.info("Elastic: {}", health.getClusterName());
        log.info(" Status: {}", health.getStatus());
        log.info("  Nodes: {}", health.getNumberOfNodes());

//		logger.info("Being Enrichment Scan");
//		var vision = ctx.getBean("visionService", VisionService.class);
//		vision.enrich();

        var tidyUp = ctx.getBean("tidyUpService", TidyUpService.class);
        tidyUp.organize();


        ctx.close();
    }

}
