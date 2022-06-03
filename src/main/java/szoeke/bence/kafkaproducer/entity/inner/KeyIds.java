package szoeke.bence.kafkaproducer.entity.inner;

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
public class KeyIds {

    public String ServedUser;
    public String Impi;
    public String Pcv;
    public String SsId;

}
