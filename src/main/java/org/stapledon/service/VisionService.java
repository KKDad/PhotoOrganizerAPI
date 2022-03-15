package org.stapledon.service;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ResourceLoader;
import org.stapledon.components.PhotoService;
import org.stapledon.dto.Photo;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
        Map<String, Photo> results = photos.scan("R:/Photos/Moments/2013-12-13");
        logger.info("Loaded {} items", results.size());

        results.forEach((path,details) -> extractLabels(path));
    }


    public Map<String, Object> extractLabels(String imageUrl) {
        var image = this.resourceLoader.getResource("file:" + imageUrl);
        AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(image, Feature.Type.LABEL_DETECTION);

        Map<String, Float> imageLabels = response.getLabelAnnotationsList().stream()
                        .collect(
                                Collectors.toMap(
                                        EntityAnnotation::getDescription,
                                        EntityAnnotation::getScore,
                                        (u, v) -> {
                                            throw new IllegalStateException(String.format("Duplicate key %s", u));
                                        },
                                        LinkedHashMap::new));

        var map = new LinkedHashMap<String, Object>();
        map.put("annotations", imageLabels);
        map.put("imageUrl", imageUrl);

        return map;
    }


}
