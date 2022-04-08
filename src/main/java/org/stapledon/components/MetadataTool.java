package org.stapledon.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.MoreFiles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stapledon.dto.Photo;
import org.stapledon.dto.takeout.PhotoDetails;
import org.stapledon.dto.vision.VisionDetails;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Component to handle loading and saving of Metadata associated with a photo downloaded from Google Takeout
 */
@Component
@Slf4j
public class MetadataTool {

    public static final String VISION_EXT = ".vision";
    public static final String TAKEOUT_EXT = ".json";
    public static final String IMAGE_EXT = ".jpg";

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Scan a path and load all photos and associated metadata
     *
     * @param basePath Path to scan
     */
    public Map<String, Photo> fetchAll(Path basePath) {
        log.info("Loading all photos and details under: {}", basePath);
        Map<String, Photo> results = new LinkedHashMap<>();

        var paths = MoreFiles.fileTraverser().breadthFirst(basePath);
        paths.forEach(path -> load(results, path));
        log.info("Loaded {} photos.", results.size());

        return results;
    }

    /**
     * Save all metadata associated with photos. Does not modify the actual photo itself
     */
    public boolean persistAll(Map<String, Photo> photos) {

        AtomicBoolean result = new AtomicBoolean(true);
        photos.forEach((photo, details) -> {
            if (!persist(details))
                result.set(false);
        });
        return result.get();
    }

    private boolean persist(Photo details) {
        if (details.isTakeOutDetailsModified()) {
            savePhotoTakeout(details.getTakeOutDetails(), details.getTakeOutDetailsPath());
            details.setTakeOutDetailsModified(false);
        }
        if (details.isVisionDetailsModified()) {
            if (details.getVisionDetailsPath() == null) {
                details.setVisionDetailsPath(new File(details.getImagePath().toString() + VISION_EXT).toPath());
            }
            saveVision(details.getVisionDetails(), details.getVisionDetailsPath());
            details.setVisionDetailsModified(false);
        }
        return false;
    }


    private void load(Map<String, Photo> results, Path path) {
        var fileName = path.getFileName().toString().toLowerCase(Locale.ROOT);
        var fileExt = fileName.lastIndexOf('.') > 0 ? fileName.substring(fileName.lastIndexOf('.')) : "";
        // If the basename has more then one extension, then keep stripping it back
        var baseName = fileName;
        do {
            if (baseName.contains("."))
                baseName = baseName.substring(0, baseName.indexOf('.'));
        } while (baseName.contains("."));

        // Skip any Metadata entries or directories
        if (baseName.equalsIgnoreCase("metadata") || path.toFile().isDirectory())
            return;


        var photo = results.computeIfAbsent(baseName, name -> Photo.builder().name(name).basePath(path.getParent()).build());

        switch (fileExt) {
            case TAKEOUT_EXT:
                var photoDetails = loadPhotoTakeout(path);
                photo.setTakeOutDetails(photoDetails);
                photo.setTakeOutDetailsPath(path);
                break;
            case IMAGE_EXT:
                photo.setImagePath(path);
                break;
            case VISION_EXT:
                var visionDetails = loadVision(path);
                photo.setVisionDetails(visionDetails);
                photo.setVisionDetailsPath(path);
                break;
            default:
                log.warn("Unknown Extension: {} : {}", fileExt, path);
        }
    }

    private PhotoDetails loadPhotoTakeout(Path path) {
        log.debug("Loading vision data: {}", path);
        try {
            return objectMapper.readValue(path.toFile(), PhotoDetails.class);
        } catch (IOException e) {
            log.error("Failed to Load photo takeout details: {}", e.getLocalizedMessage());
        }
        return null;
    }

    private void savePhotoTakeout(PhotoDetails takeOutDetails, Path takeOutDetailsPath) {
        try {
            objectMapper.writeValue(takeOutDetailsPath.toFile(), takeOutDetails);
        } catch (IOException e) {
            log.error("Failed to Save photo takeout details: {}", e.getLocalizedMessage());
        }
    }

    private VisionDetails loadVision(Path path) {
        log.debug("Loading vision data: {}", path);
        try {
            return objectMapper.readValue(path.toFile(), VisionDetails.class);
        } catch (IOException e) {
            log.error("Failed to Load vision: {}", e.getLocalizedMessage());
        }
        return null;
    }

    private void saveVision(VisionDetails visionDetails, Path visionDetailsPath) {
        try {
            objectMapper.writeValue(visionDetailsPath.toFile(), visionDetails);
        } catch (IOException e) {
            log.error("Failed to Save vision details: {}", e.getLocalizedMessage());
        }
    }
}
