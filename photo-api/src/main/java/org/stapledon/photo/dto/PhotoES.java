
package org.stapledon.photo.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.Valid;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "title",
    "description",
    "imageViews",
    "creationTime",
    "modificationTime",
    "geoData",
    "geoDataExif",
    "photoTakenTime"
})
public class PhotoES implements Serializable
{
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("imageViews")
    private String imageViews;
    @JsonProperty("creationTime")
    @Valid
    private long creationTime;
    @JsonProperty("modificationTime")
    @Valid
    private long modificationTime;
    @JsonProperty("geoData")
    @Valid
    private GeoPoint geoData;
    @JsonProperty("geoDataExif")
    @Valid
    private GeoPoint geoDataExif;
    @JsonProperty("photoTakenTime")
    @Valid
    private long photoTakenTime;

    private static final long serialVersionUID = 5091697974315856572L;

    /**
     * No args constructor for use in serialization
     *
     */
    public PhotoES() {
    }

    /**
     *
     * @param photoTakenTime
     * @param creationTime
     * @param modificationTime
     * @param description
     * @param geoDataExif
     * @param title
     * @param geoData
     * @param imageViews
     */
    public PhotoES(String title, String description, String imageViews, long creationTime, long modificationTime, GeoPoint geoData, GeoPoint geoDataExif, long photoTakenTime) {
        super();
        this.title = title;
        this.description = description;
        this.imageViews = imageViews;
        this.creationTime = creationTime;
        this.modificationTime = modificationTime;
        this.geoData = geoData;
        this.geoDataExif = geoDataExif;
        this.photoTakenTime = photoTakenTime;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("imageViews")
    public String getImageViews() {
        return imageViews;
    }

    @JsonProperty("imageViews")
    public void setImageViews(String imageViews) {
        this.imageViews = imageViews;
    }

    @JsonProperty("creationTime")
    public long getCreationTime() {
        return creationTime;
    }

    @JsonProperty("creationTime")
    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    @JsonProperty("modificationTime")
    public long getModificationTime() {
        return modificationTime;
    }

    @JsonProperty("modificationTime")
    public void setModificationTime(long modificationTime) {
        this.modificationTime = modificationTime;
    }

    @JsonProperty("geoData")
    public GeoPoint getGeoData() {
        return geoData;
    }

    @JsonProperty("geoData")
    public void setGeoData(GeoPoint geoData) {
        this.geoData = geoData;
    }

    @JsonProperty("geoDataExif")
    public GeoPoint getGeoDataExif() {
        return geoDataExif;
    }

    @JsonProperty("geoDataExif")
    public void setGeoDataExif(GeoPoint geoDataExif) {
        this.geoDataExif = geoDataExif;
    }

    @JsonProperty("photoTakenTime")
    public long getPhotoTakenTime() {
        return photoTakenTime;
    }

    @JsonProperty("photoTakenTime")
    public void setPhotoTakenTime(long photoTakenTime) {
        this.photoTakenTime = photoTakenTime;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(photoTakenTime).append(creationTime).append(modificationTime).append(description).append(geoDataExif).append(title).append(geoData).append(imageViews).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PhotoES)) {
            return false;
        }
        PhotoES rhs = ((PhotoES) other);
        return new EqualsBuilder().append(photoTakenTime, rhs.photoTakenTime).append(creationTime, rhs.creationTime).append(modificationTime, rhs.modificationTime).append(description, rhs.description).append(geoDataExif, rhs.geoDataExif).append(title, rhs.title).append(geoData, rhs.geoData).append(imageViews, rhs.imageViews).isEquals();
    }

    @JsonIgnore
    public String toJson() throws JsonProcessingException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

}
