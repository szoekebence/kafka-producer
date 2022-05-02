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
public class AppId {

    public String Type;
    public String Role;
    public String PayLoad;
    public String NodeId;
    public String SwVersion;

}
