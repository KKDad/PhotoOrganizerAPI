package org.stapledon.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import com.google.common.io.MoreFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.stapledon.dto.Photo;
import org.stapledon.dto.takeout.PhotoDetails;
import org.stapledon.dto.vision.VisionDetails;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class PhotoService {

    private static final Logger logger = LoggerFactory.getLogger(PhotoService.class);

    private static final boolean dryRun = false;

    /**
     * Scan an path and return all the detected photos
     *
     * @param basePath Path to scan
     */
    public Map<String, Photo> scan(Path basePath) {
        logger.info("Loading all photos under: {}", basePath);
        Map<String, Photo> results = new LinkedHashMap<>();
        var paths = MoreFiles.fileTraverser().breadthFirst(basePath);
        paths.forEach(path -> load(results, path));
        return results;
    }

    private void load(Map<String, Photo> results, Path path) {
        var baseName = Files.getNameWithoutExtension(path.toString().toLowerCase(Locale.ROOT));

        // Skip any Metadata entries or directories
        if (baseName.equalsIgnoreCase("metadata") || path.toFile().isDirectory())
            return;

        // If the basename has more then one extension, then keep stripping it back
        if (baseName.contains("."))
            baseName = baseName.substring(0, baseName.indexOf('.'));
        var photo = results.computeIfAbsent(baseName, name -> Photo.builder()
                .name(name)
                .pathList(new ArrayList<>())
                .build());
        photo.getPathList().add(path);
        if (path.toString().endsWith(".jpg")) {
            var photoDetails = loadPhoto(path);
            photo.setTakeOutDetails(photoDetails);

            var visionDetails = loadVision(path);
            photo.setVisionDetails(visionDetails);
        }
    }

    public PhotoDetails loadPhoto(Path path) {
        logger.debug("Loading photo: {}", path);

        var cached = new File(path + ".json");

        // Check if there is a cached Photo JSON, then use it
        if (cached.isFile()) {
            try (var bufferedReader = new BufferedReader(new FileReader(cached))) {
                return new ObjectMapper().readValue(bufferedReader, PhotoDetails.class);
            } catch (IOException ioe) {
                logger.error("Failed to Load photo: {}", ioe.getLocalizedMessage());
                return null;
            }
        }
        return null;
    }

    public VisionDetails loadVision(Path path) {
        logger.debug("Loading vision data: {}", path);

        var cached = new File(path + ".vision");

        // Check if there is a cached Photo JSON, then use it
        if (cached.isFile()) {
            try (var bufferedReader = new BufferedReader(new FileReader(cached))) {
                return new ObjectMapper().readValue(bufferedReader, VisionDetails.class);
            } catch (IOException ioe) {
                logger.error("Failed to Load vision details: {}", ioe.getLocalizedMessage());
                return null;
            }
        }
        return null;
    }

}
