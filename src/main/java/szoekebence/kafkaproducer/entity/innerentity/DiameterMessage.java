package szoekebence.kafkaproducer.entity.innerentity;

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
public class DiameterMessage {

    public Long Time;
    public String Direction;
    public String Cmd;
    public String SessionId;
    public Message Message;
}
