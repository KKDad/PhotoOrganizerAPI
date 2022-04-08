package org.stapledon.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.stapledon.components.MetadataTool;
import org.stapledon.components.organizers.IOrganizer;
import org.stapledon.dto.Photo;

import java.nio.file.Path;
import java.util.Map;

@Service
@Slf4j
public class TidyUpService {

    @Value("${verbose:true}")
    private boolean verbose;

    @Autowired
    Map<String, IOrganizer> organizers;

    @Autowired
    MetadataTool photos;

    public void organize()
    {
        var basePath = Path.of("R:/Photos/Moments/2019-10-12");
        Map<String, Photo> results = photos.fetchAll(basePath);
        results.forEach((name, photo) -> doOrganize(photo));
    }

    private void doOrganize(Photo photo) {
        var selected = organizers.get("yearMonthOrganizer");

        log.info("Processing {}", photo.getName());

        var result = selected.choose(photo);
        log.info("Moving {} from {} to {}", photo.getName(), photo.getImagePath(), result.toString());

    }
}
