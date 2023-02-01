
package org.stapledon.dto.takeout;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Data
@Builder
@Jacksonized
public class GeoData implements Serializable
{
    Double latitude;
    Double longitude;
    Double altitude;
    Double latitudeSpan;
    Double longitudeSpan;
}
