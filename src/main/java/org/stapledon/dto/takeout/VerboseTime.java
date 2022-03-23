
package org.stapledon.dto.takeout;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Data
@Builder
@Jacksonized
public class VerboseTime implements Serializable
{

    @JsonProperty("timestamp")
    public String timestamp;

    @JsonProperty("formatted")
    public String formatted;
}
