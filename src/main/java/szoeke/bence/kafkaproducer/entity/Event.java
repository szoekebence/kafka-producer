package szoeke.bence.kafkaproducer.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import szoeke.bence.kafkaproducer.entity.inner.EventInfo;
import szoeke.bence.kafkaproducer.entity.inner.EventRecordHeader;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event implements Serializable {

    public EventRecordHeader eventRecordHeader;
    public EventInfo eventInfo;

}
