
package org.stapledon.photo.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhotoES implements Serializable
{
    @JsonProperty("title")
    public String title;

    @JsonProperty("description")
    public String description;

    @JsonProperty("imageViews")
    public String imageViews;

    @JsonProperty("creationTime")
    @Valid
    public long creationTime;

    @JsonProperty("modificationTime")
    @Valid
    public long modificationTime;

    @JsonProperty("geoData")
    @Valid
    public GeoPoint geoData;

    @JsonProperty("geoDataExif")
    @Valid
    public GeoPoint geoDataExif;

    @JsonProperty("photoTakenTime")
    @Valid
    public long photoTakenTime;

    @JsonIgnore
    public String toJson() throws JsonProcessingException
    {
        var objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

}
