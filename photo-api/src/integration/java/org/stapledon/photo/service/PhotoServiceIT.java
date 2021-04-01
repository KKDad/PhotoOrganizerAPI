package org.stapledon.photo.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.stapledon.photo.dto.Photo;

import java.io.File;
import java.util.List;

class PhotoServiceIT {

    @Test
    void testLoadPhoto() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        String path = new File(classLoader.getResource("FB_IMG_13869672665371829.jpg.json").getPath()).getParent();

        PhotoService subject = new PhotoService();
        Photo result = subject.loadPhoto(path + "/FB_IMG_13869672665371829.jpg");

        Assertions.assertNotNull(result);
        Assertions.assertEquals("FB_IMG_13869672665371829.jpg", result.getTitle());
        Assertions.assertEquals("1386967266", result.getPhotoTakenTime().getTimestamp());
    }


    @Test
    void testLoadDirectory() {
        PhotoService subject = new PhotoService();
        List<Photo> results = subject.load("R:/Drive/Moments/2013-12-13");

        Assertions.assertNotNull(results);
        Assertions.assertEquals(2, results.size());
        Assertions.assertTrue(results.get(0).getTitle().contains("FB_IMG_13869"));
    }
}