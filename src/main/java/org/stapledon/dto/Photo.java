
package org.stapledon.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Photo implements Serializable
{
    @JsonProperty("title")
    public String title;

    @JsonProperty("description")
    public String description;

    @JsonProperty("imageViews")
    public String imageViews;

    @JsonProperty("creationTime")
    public VerboseTime creationTime;

    @JsonProperty("modificationTime")
    public VerboseTime modificationTime;

    @JsonProperty("geoData")
    public GeoData geoData;

    @JsonProperty("geoDataExif")
    public GeoData geoDataExif;

    @JsonProperty("photoTakenTime")
    public VerboseTime photoTakenTime;

    @JsonIgnore
    public String toJson() throws JsonProcessingException
    {
        var objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    @JsonIgnore
    public LocalDateTime getLocalDate() {
        if (this.photoTakenTime != null && Long.parseLong(this.photoTakenTime.timestamp) > 0L) {
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(this.photoTakenTime.timestamp)), TimeZone.getDefault().toZoneId());
        }
        return LocalDateTime.now();
    }
}
