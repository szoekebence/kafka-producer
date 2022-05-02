package szoeke.bence.kafkaproducer.entity.innerentity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response extends Request {

    public Long Result;
    public String OrigRealm;

    public Response setResult(Long result) {
        Result = result;
        return this;
    }

    public Response setOrigRealm(String origRealm) {
        OrigRealm = origRealm;
        return this;
    }
}
