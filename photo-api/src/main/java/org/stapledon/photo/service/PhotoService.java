package org.stapledon.photo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stapledon.photo.dto.GeoPoint;
import org.stapledon.photo.dto.Photo;
import org.stapledon.photo.dto.PhotoES;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PhotoService {

    private static final Logger logger = LoggerFactory.getLogger(PhotoService.class);

    final boolean dryRun;

    public PhotoService(boolean dryRun) {
        this.dryRun = dryRun;
    }


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

    public PhotoES convert(Photo photo) {
        var photoES = new PhotoES();
        photoES.title = photo.title;
        photoES.description =photo.description;
        photoES.imageViews = photo.imageViews;
        photoES.creationTime =Long.parseLong(photo.creationTime.timestamp);
        photoES.modificationTime = Long.parseLong(photo.modificationTime.timestamp);
        photoES.geoData = new GeoPoint(photo.geoData.latitude, photo.geoData.longitude);
        photoES.geoDataExif  = new GeoPoint(photo.geoDataExif.latitude, photo.geoDataExif.longitude);
        photoES.photoTakenTime = Long.parseLong(photo.photoTakenTime.timestamp);

        return photoES;
    }

    public List<Photo> load(String path) {
        return load(path, Integer.MAX_VALUE);
    }

    public List<Photo> load(String path, int maxDocs) {
        logger.info("Loading all photos under: {}", path);
        List<Photo> results = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.map(Path::toString)
                    .filter(f -> f.endsWith(".jpg"))
                    .takeWhile(p -> (results.size() <= maxDocs))
                    .forEach(p -> {
                        var photo = loadPhoto(p);
                        if (photo != null) {
                            results.add(photo);
                        }
                    });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
        return results;
    }

}
