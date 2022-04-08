package org.stapledon.service;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.stapledon.components.MetadataTool;
import org.stapledon.dto.Photo;
import org.stapledon.dto.vision.DominantColors;
import org.stapledon.dto.vision.LabelAnnotation;
import org.stapledon.dto.vision.VisionDetails;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class VisionService {

    private static final Logger logger = LoggerFactory.getLogger(VisionService.class);

    @Autowired
    MetadataTool photos;

    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;

    @Autowired
    private ResourceLoader resourceLoader;

    public void enrich() {
        logger.info("Enriching photos");

        var basePath = Path.of("R:/Photos/Moments/2019-10-12");
        Map<String, Photo> results = photos.fetchAll(basePath);
        results.forEach((name, photo) -> performDetection(photo));
        photos.persistAll(results);
    }


    /**
     * Call Google Vision API to analyze a single image and perform Label and Image analysis.
     *
     * @param photo Photo to perform vision detection on
     * @return True if the detection is successfully performed
     */
    public boolean performDetection(Photo photo) {
        if (photo.getImagePath() == null || (photo.getVisionDetails() != null && photo.getVisionDetails().isProcessed()))
            return false;

        var image = this.resourceLoader.getResource("file:" + photo.getImagePath());
        AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(image, Feature.Type.LABEL_DETECTION, Feature.Type.IMAGE_PROPERTIES);

        if (!response.hasError()) {
            var details = new VisionDetails();
            details.setResultDate(LocalDateTime.now());
            details.setProcessed(true);
            if (!response.getLabelAnnotationsList().isEmpty())
                response.getLabelAnnotationsList().forEach(item ->
                        details.getLabels().add(LabelAnnotation.builder()
                                .mid(item.getMid())
                                .score(item.getScore())
                                .description(item.getDescription())
                                .topicality(item.getTopicality())
                                .build()));
            response.getImagePropertiesAnnotation().getDominantColors().getColorsList().forEach(item ->
                    details.getColors().add(DominantColors.builder()
                            .blue(item.getColor().getBlue())
                            .red(item.getColor().getRed())
                            .green(item.getColor().getGreen())
                            .pixelFraction(item.getPixelFraction())
                            .score(item.getScore())
                            .build()));
                photo.setVisionDetails(details);
                photo.setVisionDetailsModified(true);
            return true;
        }
        return false;
    }
}
