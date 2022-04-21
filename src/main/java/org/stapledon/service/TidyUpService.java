package org.stapledon.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stapledon.components.MetadataTool;
import org.stapledon.components.duplicates.IDupDetector;
import org.stapledon.components.organizers.IOrganizer;
import org.stapledon.configuration.properties.OrganizerProperties;
import org.stapledon.dto.Photo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Service
@Slf4j
public class TidyUpService {

    @Autowired
    OrganizerProperties config;

    @Autowired
    Map<String, IOrganizer> organizers;

    @Autowired
    Map<String, IDupDetector> dupDetector;

    @Autowired
    MetadataTool photos;

    public void organize() {
        Map<String, Photo> results = photos.fetchAll(config.getTakeout().getExports());
        results.forEach((name, photo) -> doOrganize(photo));
    }

    protected void doOrganize(Photo photo) {
        var selected = organizers.get("yearMonthOrganizer");

        log.info("Processing {}", photo.getName());

        var result = selected.choosePath(photo);
        if (result == null) {
            log.warn("No destination chosen for {}", photo.getImagePath());
        } else
            try {
                // Ensure destination exists
                createDirectory(result);

                // Copy/Move the files that compose of the photo and it's metadata
                photo.setImagePath(copyMove(photo.getImagePath(), result));
                photo.setTakeOutDetailsPath(copyMove(photo.getTakeOutDetailsPath(), result));
                photo.setTakeOutDetailsPath(copyMove(photo.getVisionDetailsPath(), result));

                // Clean up original file location
                removeIfEmpty(photo.getBasePath());

            } catch (IOException ie) {
                log.error("Unable to organize {}: {}", photo.getName(), ie.getLocalizedMessage());
                throw new TidyUpException(ie.getLocalizedMessage(), ie);
            }
    }

    /**
     * If the directory is empty, delete it
     *
     * @param target - Path to delete
     */
    protected void removeIfEmpty(Path target) {
        try {
            var fileList = target.toFile().list();
            if (fileList != null && fileList.length == 0) {
                if (config.getTidyUp().isVerbose())
                    log.info("Removing empty folder {}", target);
                if (!config.getTidyUp().isDryRun())
                    Files.delete(target);
            }
        } catch (IOException ie) {
            log.error(ie.getLocalizedMessage());
        }
    }

    protected void createDirectory(Path storagePath) throws IOException {
        if (storagePath != null && !Files.exists(storagePath)) {
            if (config.getTidyUp().isVerbose())
                log.info("Creating {}", storagePath);
            if (!config.getTidyUp().isDryRun())
                Files.createDirectories(storagePath);
        }
    }

    protected Path copyMove(Path path, Path storagePath) throws IOException {
        if (path == null)
            return null;

        var destPath = Path.of(storagePath.toString(), path.getFileName().toString());
        if (config.getTidyUp().isCopy()) {
            log.info("Copying {} -> {}", path, destPath);
            if (!config.getTidyUp().isDryRun())
                Files.copy(path, destPath);
        } else {
            log.info("Moving {} -> {}", path, destPath);
            if (!config.getTidyUp().isDryRun())
                Files.move(path, destPath);
        }
        return destPath;
    }
}
