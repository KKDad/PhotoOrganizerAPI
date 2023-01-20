
package org.stapledon.dto.takeout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@Jacksonized
public class PhotoDetails implements Serializable
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
    public LocalDateTime getLocalDate() {
        if (this.photoTakenTime != null && this.photoTakenTime.timestamp != null) {
            return this.photoTakenTime.toLocalDateTime();
        }
        return LocalDateTime.now();
    }
}
