package org.stapledon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.stapledon.service.TidyUpService;

import java.io.IOException;

@SpringBootApplication
@Slf4j
public class PhotoOrganizerApplication {

    public static void main(String[] args) throws IOException {
        var ctx = SpringApplication.run(PhotoOrganizerApplication.class, args);

//		var elastic = ctx.getBean("restHighLevelClient", RestHighLevelClient.class);
//		logger.info("Elastic: {}", elastic.cluster().health(new ClusterHealthRequest(), RequestOptions.DEFAULT).getClusterName());

//		logger.info("Being Enrichment Scan");
//		var vision = ctx.getBean("visionService", VisionService.class);
//		vision.enrich();

        var tidyUp = ctx.getBean("tidyUpService", TidyUpService.class);
        tidyUp.organize();


        ctx.close();
    }

}
