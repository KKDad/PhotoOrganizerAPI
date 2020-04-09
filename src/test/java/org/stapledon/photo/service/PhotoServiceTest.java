package org.stapledon.photo.service;

import org.junit.Assert;
import org.junit.Test;
import org.stapledon.photo.dto.Photo;

import java.io.File;

import static org.junit.Assert.*;

public class PhotoServiceTest {

    @Test
    public void loadPhoto()
    {
        ClassLoader classLoader = this.getClass().getClassLoader();
        String path = new File(classLoader.getResource("FB_IMG_13869672665371829.jpg.json").getPath()).getParent();

        PhotoService subject = new PhotoService();
        Photo result = subject.loadPhoto(path + "/FB_IMG_13869672665371829.jpg");

        Assert.assertNotNull(result);
        Assert.assertEquals("FB_IMG_13869672665371829.jpg", result.getTitle());
        Assert.assertEquals("1386967266", result.getPhotoTakenTime().getTimestamp());
    }
}