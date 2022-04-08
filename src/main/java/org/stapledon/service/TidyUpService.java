package org.stapledon.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.stapledon.components.MetadataTool;
import org.stapledon.components.organizers.IOrganizer;
import org.stapledon.dto.Photo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Service
@Slf4j
public class TidyUpService {

    @Value("${verbose:true}")
    private boolean verbose;

    @Value("${is-copy:true}")
    private boolean isCopy;

    @Value("${dry-run:true}")
    private boolean dryRun;

    @Autowired
    Map<String, IOrganizer> organizers;

    @Autowired
    MetadataTool photos;

    public void organize() {
        var basePath = Path.of("R:/Photos/Moments");
        Map<String, Photo> results = photos.fetchAll(basePath);
        results.forEach((name, photo) -> doOrganize(photo));
    }

    private void doOrganize(Photo photo) {
        var selected = organizers.get("yearMonthOrganizer");

        log.info("Processing {}", photo.getName());

        var result = selected.choose(photo);
        if (result == null) {
            log.warn("No destination chosen for {}", photo.getImagePath());
        } else
            try {
                // Ensure destination exists
                createDirectory(result);

                photo.setImagePath(copyMove(photo.getImagePath(), result));
                photo.setTakeOutDetailsPath(copyMove(photo.getTakeOutDetailsPath(), result));
                photo.setTakeOutDetailsPath(copyMove(photo.getVisionDetailsPath(), result));

            } catch (IOException ie) {
                log.error(ie.getLocalizedMessage());
                throw new TidyUpException(ie.getLocalizedMessage(), ie);
            }
    }

    private void createDirectory(Path storagePath) throws IOException {
        if (storagePath != null && !Files.exists(storagePath)) {
            if (verbose)
                log.info("Creating {}", storagePath);
            if (!dryRun)
                Files.createDirectories(storagePath);
        }
    }

    private Path copyMove(Path path, Path storagePath) throws IOException {
        if (path == null)
            return null;

        var destPath = Path.of(storagePath.toString(), path.getFileName().toString());
        if (isCopy) {
            log.info("Copying {} -> {}", path, destPath);
            if (!dryRun)
                Files.copy(path, destPath);
        } else {
            log.info("Moving {} -> {}", path, destPath);
            if (!dryRun)
                Files.move(path, destPath);
        }
        return destPath;
    }
}
