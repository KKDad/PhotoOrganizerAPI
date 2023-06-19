
package org.stapledon.dto.takeout;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class GeoData implements Serializable
{
    Double latitude;
    Double longitude;
    Double altitude;
    Double latitudeSpan;
    Double longitudeSpan;
}
