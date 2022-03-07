
package org.stapledon.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeoData implements Serializable
{

    @JsonProperty("latitude")
    public Double latitude;

    @JsonProperty("longitude")
    public Double longitude;

    @JsonProperty("altitude")
    public Double altitude;

    @JsonProperty("latitudeSpan")
    public Double latitudeSpan;

    @JsonProperty("longitudeSpan")
    public Double longitudeSpan;
}
