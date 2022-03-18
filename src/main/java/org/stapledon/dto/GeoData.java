
package org.stapledon.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class GeoData implements Serializable
{
    Double latitude;
    Double longitude;
    Double altitude;
    Double latitudeSpan;
    Double longitudeSpan;
}
