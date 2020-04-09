package org.stapledon.photo.api;

import com.codahale.metrics.annotation.Timed;
import org.stapledon.photo.dto.Photo;
import org.stapledon.photo.service.PhotoService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@Path("/media")
@Produces(MediaType.APPLICATION_JSON)
public class ImageProcessor
{
    private final AtomicLong counter;

    public ImageProcessor() {
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    @Path("/photo")
    public Photo photo(@QueryParam("path") String path)
    {
        counter.incrementAndGet();

        Photo photo = new PhotoService().loadPhoto(path);
        if (photo != null)
            return photo;

        throw new NotFoundException();
    }

    @GET
    @Timed
    @Path("/search")
    public List<Photo> photos(@QueryParam("path") String path)
    {
        counter.incrementAndGet();
        return new PhotoService().load(path);
    }

}






