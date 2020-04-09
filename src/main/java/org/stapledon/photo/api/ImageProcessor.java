package org.stapledon.photo.api;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stapledon.photo.dto.Photo;
import org.stapledon.photo.service.PhotoService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;


@Path("/media")
@Produces(MediaType.APPLICATION_JSON)
public class ImageProcessor
{
    private final AtomicLong counter;
    //private static final Logger logger = LoggerFactory.getLogger(ImageProcessor.class);

    public ImageProcessor() {
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Photo photo(@QueryParam("path") String path)
    {
        counter.incrementAndGet();

        Photo photodetails = new PhotoService().loadPhoto(path);
        if (photodetails != null)
            return photodetails;

        throw new NotFoundException();
    }

}






