
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
    "latitude",
    "longitude",
    "altitude",
    "latitudeSpan",
    "longitudeSpan"
})
public class GeoDataExif implements Serializable
{

    @JsonProperty("latitude")
    private Double latitude;
    @JsonProperty("longitude")
    private Double longitude;
    @JsonProperty("altitude")
    private Double altitude;
    @JsonProperty("latitudeSpan")
    private Double latitudeSpan;
    @JsonProperty("longitudeSpan")
    private Double longitudeSpan;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 7597248044966103999L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public GeoDataExif() {
    }

    /**
     * 
     * @param altitude
     * @param longitudeSpan
     * @param latitude
     * @param latitudeSpan
     * @param longitude
     */
    public GeoDataExif(Double latitude, Double longitude, Double altitude, Double latitudeSpan, Double longitudeSpan) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.latitudeSpan = latitudeSpan;
        this.longitudeSpan = longitudeSpan;
    }

    @JsonProperty("latitude")
    public Double getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("longitude")
    public Double getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("altitude")
    public Double getAltitude() {
        return altitude;
    }

    @JsonProperty("altitude")
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    @JsonProperty("latitudeSpan")
    public Double getLatitudeSpan() {
        return latitudeSpan;
    }

    @JsonProperty("latitudeSpan")
    public void setLatitudeSpan(Double latitudeSpan) {
        this.latitudeSpan = latitudeSpan;
    }

    @JsonProperty("longitudeSpan")
    public Double getLongitudeSpan() {
        return longitudeSpan;
    }

    @JsonProperty("longitudeSpan")
    public void setLongitudeSpan(Double longitudeSpan) {
        this.longitudeSpan = longitudeSpan;
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
        return new HashCodeBuilder().append(altitude).append(longitudeSpan).append(latitude).append(latitudeSpan).append(additionalProperties).append(longitude).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GeoDataExif) == false) {
            return false;
        }
        GeoDataExif rhs = ((GeoDataExif) other);
        return new EqualsBuilder().append(altitude, rhs.altitude).append(longitudeSpan, rhs.longitudeSpan).append(latitude, rhs.latitude).append(latitudeSpan, rhs.latitudeSpan).append(additionalProperties, rhs.additionalProperties).append(longitude, rhs.longitude).isEquals();
    }

}
