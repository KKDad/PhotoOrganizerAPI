package org.stapledon.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.stapledon.dto.Photo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Component
public class PhotoService {

    private static final Logger logger = LoggerFactory.getLogger(PhotoService.class);

    private static final boolean dryRun = false;

    public Photo loadPhoto(String path) {
        logger.debug("Loading photo: {}", path);

        var cached = new File(path + ".json");

        // Check if there is a cached Photo JSON, then use it
        if (cached.isFile()) {
            try (var bufferedReader = new BufferedReader(new FileReader(cached))) {
                return new ObjectMapper().readValue(bufferedReader, Photo.class);
            } catch (IOException ioe) {
                logger.error("Failed to Load photo: {}", ioe.getLocalizedMessage());
                return null;
            }
        }
        return null;
    }

    /**
     * Scan an path and return all the detected photos
     * @param path Path to scan
     */
    public Map<String,Photo> scan(Path path) {
        return scan(path, Integer.MAX_VALUE);
    }

    public Map<String,Photo> scan(Path path, int maxDocs) {
        logger.info("Loading all photos under: {}", path);
        Map<String,Photo> results = new LinkedHashMap<>();

        try (Stream<Path> paths = Files.walk(path)) {
            paths.map(Path::toString)
                    .filter(f -> f.toLowerCase(Locale.ROOT).endsWith(".jpg"))
                    .takeWhile(p -> (results.size() <= maxDocs))
                    .forEach(p -> {
                        var photo = loadPhoto(p);
                        if (photo != null) {
                            results.put(p, photo);
                        }
                    });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
        return results;
    }
}
