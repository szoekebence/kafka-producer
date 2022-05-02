package szoeke.bence.kafkaproducer.entity.innerentity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {

    public String OrigHost;
    public String DestRealm;
    public Long DiamAppId;
    public AdditionalInfo AdditionalInfo;
}
