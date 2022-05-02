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
public class EventRecordHeader {

    public Long EventId;
    public Long StartTime;
    public Long EndTime;
    public String SchemaVersion;
    public Cause Cause;
    public Long Result;
    public AppId AppId;
    public KeyIds KeyIds;

}
