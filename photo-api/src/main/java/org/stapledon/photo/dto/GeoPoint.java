
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
        "lat",
        "lon",
})
public class GeoPoint implements Serializable
{

    @JsonProperty("lat")
    private Double latitude;
    @JsonProperty("lon")
    private Double longitude;

    private static final long serialVersionUID = 3592107043786475855L;

    /**
     * No args constructor for use in serialization
     *
     */
    public GeoPoint() {
    }

    /**
     *
     * @param latitude
     * @param longitude
     */
    public GeoPoint(Double latitude, Double longitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @JsonProperty("lat")
    public Double getLatitude() {
        return latitude;
    }

    @JsonProperty("lat")
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("lon")
    public Double getLongitude() {
        return longitude;
    }

    @JsonProperty("lon")
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(latitude).append(longitude).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GeoPoint)) {
            return false;
        }
        GeoPoint rhs = ((GeoPoint) other);
        return new EqualsBuilder().append(latitude, rhs.latitude).append(longitude, rhs.longitude).isEquals();
    }

}
