package org.stapledon.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.stapledon.components.MetadataTool;
import org.stapledon.components.duplicates.IDupDetector;
import org.stapledon.components.organizers.IOrganizer;
import org.stapledon.dto.Photo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
@Slf4j
public class TidyUpService {

    @Value("${tidyup/verbose:true}")
    private boolean verbose;

    @Value("${tidyup.is-copy:true}")
    private boolean isCopy;

    @Value("${tidyup.dry-run:true}")
    private boolean dryRun;

    @Value("${takeout.exports}")
    private Path sourcePath;


    @Autowired
    Map<String, IOrganizer> organizers;

    @Autowired
    Map<String, IDupDetector> dupDetector;

    @Autowired
    MetadataTool photos;

    public void organize() {
        Map<String, Photo> results = photos.fetchAll(sourcePath);
        results.forEach((name, photo) -> doOrganize(photo));
    }

    private void doOrganize(Photo photo) {
        var selected = organizers.get("yearMonthOrganizer");

        log.info("Processing {}", photo.getName());

        var result = selected.choosePath(photo);
        if (result == null) {
            log.warn("No destination chosen for {}", photo.getImagePath());
        } else
            try {
                // Ensure destination exists
                createDirectory(result);

                photo.setImagePath(copyMove(photo.getImagePath(), result));
                photo.setTakeOutDetailsPath(copyMove(photo.getTakeOutDetailsPath(), result));
                photo.setTakeOutDetailsPath(copyMove(photo.getVisionDetailsPath(), result));

//                var parent = Path.of(photo.getFolder());
//                if (parent.toFile().list().length == 0) {
//                    if (verbose)
//                        log.info("Removing empty folder {}", photo.getFolder());
//                    if (!dryRun)
//                        Files.delete(parent);
//                }

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
