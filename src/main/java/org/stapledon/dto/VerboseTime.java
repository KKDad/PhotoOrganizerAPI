
package org.stapledon.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerboseTime implements Serializable
{

    @JsonProperty("timestamp")
    public String timestamp;

    @JsonProperty("formatted")
    public String formatted;
}
