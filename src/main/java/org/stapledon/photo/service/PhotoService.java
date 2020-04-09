package org.stapledon.photo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stapledon.photo.dto.Photo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

}
