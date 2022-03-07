package org.stapledon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.stapledon.components.PhotoService;
import org.stapledon.dto.Photo;

import java.util.List;

@SpringBootApplication
public class PhotoOrganizerApplication {

	private static final Logger logger = LoggerFactory.getLogger(PhotoOrganizerApplication.class);


	public static void main(String[] args) {
		var ctx = SpringApplication.run(PhotoOrganizerApplication.class, args);

		var photoService = ctx.getBean("photoService", PhotoService.class);
		List<Photo> results = photoService.load("R:/Drive/Moments/2013-12-13");
		logger.info("Loaded {} items", results.size());

		ctx.close();
	}

}
