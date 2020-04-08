
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
    "timestamp",
    "formatted"
})
public class ModificationTime implements Serializable
{

    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("formatted")
    private String formatted;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 5014733211506823890L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ModificationTime() {
    }

    /**
     * 
     * @param formatted
     * @param timestamp
     */
    public ModificationTime(String timestamp, String formatted) {
        super();
        this.timestamp = timestamp;
        this.formatted = formatted;
    }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("formatted")
    public String getFormatted() {
        return formatted;
    }

    @JsonProperty("formatted")
    public void setFormatted(String formatted) {
        this.formatted = formatted;
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
        return new HashCodeBuilder().append(additionalProperties).append(formatted).append(timestamp).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ModificationTime) == false) {
            return false;
        }
        ModificationTime rhs = ((ModificationTime) other);
        return new EqualsBuilder().append(additionalProperties, rhs.additionalProperties).append(formatted, rhs.formatted).append(timestamp, rhs.timestamp).isEquals();
    }

}
