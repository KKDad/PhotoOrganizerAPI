package org.stapledon.components.duplicates;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;
import org.stapledon.dto.Photo;
import org.stapledon.dto.takeout.PhotoDetails;
import org.stapledon.dto.takeout.VerboseTime;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Objects;

class ChecksumDetectorTest {

    @Test
    void whenGiveDifferentImage_NotDuplicateTest() throws CannotDetermineException, FileNotFoundException {
        var photo1 = Photo.builder()
                .name("Siamese")
                .imagePath(ResourceUtils.getFile("classpath:images/siamese.png").getAbsoluteFile().toPath())
                .build();
        var photo2 = Photo.builder()
                .name("Tabby")
                .imagePath(ResourceUtils.getFile("classpath:images/tabby.png").getAbsoluteFile().toPath())
                .build();

        var subject = new ChecksumDetector();
        Assertions.assertFalse(subject.isDuplicate(photo1, photo2));
    }

    @Test
    void whenGiveSameImage_IsDuplicateTest() throws CannotDetermineException, FileNotFoundException {
        var photo1 = Photo.builder()
                .name("Siamese")
                .imagePath(ResourceUtils.getFile("classpath:images/siamese.png").getAbsoluteFile().toPath())
                .build();
        var photo2 = Photo.builder()
                .name("Tabby")
                .imagePath(ResourceUtils.getFile("classpath:images/siamese.png").getAbsoluteFile().toPath())
                .build();

        var subject = new ChecksumDetector();
        Assertions.assertTrue(subject.isDuplicate(photo1, photo2));
    }
}