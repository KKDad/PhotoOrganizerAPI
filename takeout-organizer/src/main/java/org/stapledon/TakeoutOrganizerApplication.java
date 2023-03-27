package org.stapledon;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.stapledon.service.TidyUpService;

import java.io.IOException;

@SpringBootApplication
@Slf4j
public class TakeoutOrganizerApplication {

    public static void main(String[] args) throws IOException {
        var ctx = SpringApplication.run(TakeoutOrganizerApplication.class, args);

		var elastic = ctx.getBean("httpClient", RestClient.class);
        elastic.getNodes().forEach(node -> log.info("Elasticsearch Node: {}:{} - {} - {}",
                node.getHost().getHostName(),
                node.getHost().getPort(),
                node.getVersion(),
                node.getRoles()
        ));

//		logger.info("Being Enrichment Scan");
//		var vision = ctx.getBean("visionService", VisionService.class);
//		vision.enrich();

        var tidyUp = ctx.getBean("tidyUpService", TidyUpService.class);
        tidyUp.organize();


        ctx.close();
    }

}
