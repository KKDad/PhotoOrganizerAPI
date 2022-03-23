package org.stapledon.service;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.stapledon.components.PhotoService;
import org.stapledon.dto.Photo;
import org.stapledon.dto.vision.DominantColors;
import org.stapledon.dto.vision.LabelAnnotation;
import org.stapledon.dto.vision.VisionDetails;

import java.nio.file.Path;
import java.util.Map;

@Service
public class VisionService {

    private static final Logger logger = LoggerFactory.getLogger(VisionService.class);

    @Autowired
    PhotoService photos;

    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;

    @Autowired
    private ResourceLoader resourceLoader;

    public void enrich() {
        var basePath = Path.of("R:/Photos/Moments/2019-10-12");
        Map<String, Photo> results = photos.scan(basePath);
        logger.info("Loaded {} items", results.size());

        results.forEach((name, photo) -> extractLabels(photo));
    }


    public boolean extractLabels(Photo photo) {
        if (photo.imagePath() == null)
            return true;

        var image = this.resourceLoader.getResource("file:" + photo.imagePath());
        AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(image, Feature.Type.LABEL_DETECTION, Feature.Type.IMAGE_PROPERTIES);

        if (!response.hasError()) {
            var details = new VisionDetails();
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
            return true;
        }
        return false;
    }
}
