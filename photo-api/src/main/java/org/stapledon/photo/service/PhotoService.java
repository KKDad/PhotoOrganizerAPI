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


    public Photo loadPhoto(String path) {
        logger.info("Loading photo: {}", path);

        File cached = new File(path + ".json");

        // Check if there is a cached Photo JSON, then use it
        if (cached.isFile()) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(cached))) {
                return new ObjectMapper().readValue(bufferedReader, Photo.class);
            } catch (IOException ioe) {
                logger.error("Failed to Load photo: {}", ioe.getLocalizedMessage());
                return null;
            }
        }
        return null;
    }

    public PhotoES convert(Photo photo) {
        PhotoES photoES = new PhotoES();
        photoES.setTitle(photo.getTitle());
        photoES.setDescription(photo.getDescription());
        photoES.setImageViews(photo.getImageViews());
        photoES.setCreationTime(Long.valueOf(photo.getCreationTime().getTimestamp()));
        photoES.setModificationTime(Long.valueOf(photo.getModificationTime().getTimestamp()));
        photoES.setGeoData(new GeoPoint(photo.getGeoData().getLatitude(), photo.getGeoData().getLongitude()));
        photoES.setGeoDataExif(new GeoPoint(photo.getGeoDataExif().getLatitude(), photo.getGeoDataExif().getLongitude()));
        photoES.setPhotoTakenTime(Long.valueOf(photo.getPhotoTakenTime().getTimestamp()));

        return photoES;
    }

    public List<Photo> load(String path)
    {
        logger.info("Loading all photos under: {}", path);
        List<Photo> results = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.map(Path::toString)
                 .filter(f -> f.endsWith(".jpg"))
                 .forEach(p -> { Photo photo = loadPhoto(p); if (photo != null) {  results.add(photo); } });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
        return results;
    }

}
