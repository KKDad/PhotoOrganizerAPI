
package org.stapledon.photo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerboseTime implements Serializable
{

    @JsonProperty("timestamp")
    public String timestamp;

    @JsonProperty("formatted")
    public String formatted;
}
