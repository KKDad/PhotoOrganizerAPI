package org.stapledon.components.duplicates;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.MurmurHash3;
import org.springframework.stereotype.Component;
import org.stapledon.dto.Photo;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Detects duplicate photos by calculating by calculating the murmur3 checksum of the image file contents
 */
@Component
@Slf4j
public class ChecksumDetector implements IDupDetector {

    @Override
    public boolean isDuplicate(Photo left, Photo right) throws CannotDetermineException {

        try {
            var leftHash = MurmurHash3.hash128x64(Files.readAllBytes(left.getImagePath()));
            var rightHash = MurmurHash3.hash128x64(Files.readAllBytes(right.getImagePath()));
            return Arrays.equals(leftHash, rightHash);

        } catch (IOException e) {
            throw new CannotDetermineException("Cannot determine", e);
        }
    }
}
