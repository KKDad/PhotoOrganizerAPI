
package org.stapledon.photo.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
public class Photo implements Serializable
{

    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("imageViews")
    private String imageViews;
    @JsonProperty("creationTime")
    @Valid
    private CreationTime creationTime;
    @JsonProperty("modificationTime")
    @Valid
    private ModificationTime modificationTime;
    @JsonProperty("geoData")
    @Valid
    private GeoData geoData;
    @JsonProperty("geoDataExif")
    @Valid
    private GeoDataExif geoDataExif;
    @JsonProperty("photoTakenTime")
    @Valid
    private PhotoTakenTime photoTakenTime;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -1493045639300665827L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Photo() {
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
    public Photo(String title, String description, String imageViews, CreationTime creationTime, ModificationTime modificationTime, GeoData geoData, GeoDataExif geoDataExif, PhotoTakenTime photoTakenTime) {
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
    public CreationTime getCreationTime() {
        return creationTime;
    }

    @JsonProperty("creationTime")
    public void setCreationTime(CreationTime creationTime) {
        this.creationTime = creationTime;
    }

    @JsonProperty("modificationTime")
    public ModificationTime getModificationTime() {
        return modificationTime;
    }

    @JsonProperty("modificationTime")
    public void setModificationTime(ModificationTime modificationTime) {
        this.modificationTime = modificationTime;
    }

    @JsonProperty("geoData")
    public GeoData getGeoData() {
        return geoData;
    }

    @JsonProperty("geoData")
    public void setGeoData(GeoData geoData) {
        this.geoData = geoData;
    }

    @JsonProperty("geoDataExif")
    public GeoDataExif getGeoDataExif() {
        return geoDataExif;
    }

    @JsonProperty("geoDataExif")
    public void setGeoDataExif(GeoDataExif geoDataExif) {
        this.geoDataExif = geoDataExif;
    }

    @JsonProperty("photoTakenTime")
    public PhotoTakenTime getPhotoTakenTime() {
        return photoTakenTime;
    }

    @JsonProperty("photoTakenTime")
    public void setPhotoTakenTime(PhotoTakenTime photoTakenTime) {
        this.photoTakenTime = photoTakenTime;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(photoTakenTime).append(creationTime).append(modificationTime).append(description).append(geoDataExif).append(additionalProperties).append(title).append(geoData).append(imageViews).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Photo) == false) {
            return false;
        }
        Photo rhs = ((Photo) other);
        return new EqualsBuilder().append(photoTakenTime, rhs.photoTakenTime).append(creationTime, rhs.creationTime).append(modificationTime, rhs.modificationTime).append(description, rhs.description).append(geoDataExif, rhs.geoDataExif).append(additionalProperties, rhs.additionalProperties).append(title, rhs.title).append(geoData, rhs.geoData).append(imageViews, rhs.imageViews).isEquals();
    }

}
