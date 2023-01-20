
package org.stapledon.dto.takeout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@Builder
@Jacksonized
public class VerboseTime implements Serializable
{
    @JsonProperty("timestamp")
    public Long timestamp;

    @JsonProperty("formatted")
    public String formatted;

    @JsonIgnore
    public LocalDateTime toLocalDateTime()
    {
        return  LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC);
    }
}
