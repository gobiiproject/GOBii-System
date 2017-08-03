package org.gobiiproject.gobiiapimodel.payload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import java.util.List;

/**
 * Created by Phil on 9/6/2016.
 */
public class PayloadEnvelope<T> {

    public PayloadEnvelope() {
    }

    public PayloadEnvelope<T> fromJson(JsonObject jsonObject,
                                      Class<T> dtoType) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        PayloadEnvelope<T> returnVal = objectMapper.readValue(jsonObject.toString(), PayloadEnvelope.class);


        // The Jackson object mapper doesn't seem to have a means for knowing that the embedded list
        // is supposed to be cast to the DTO type. There's probably a more architectural way of doing
        // this -- e.g., a custom deserialization mechanism. But this gets the job done. Most importantly,
        // by properly casting this list of DTO objects, we prevent the Java client from caring too badly
        // about the envelope request semantics.
        JsonArray jsonArray = jsonObject.get("payload").getAsJsonObject().get("data").getAsJsonArray();
        String arrayAsString = jsonArray.toString();
        List<T> resultItemList = objectMapper.readValue(arrayAsString,
                objectMapper.getTypeFactory().constructCollectionType(List.class, dtoType));

        returnVal.getPayload().setData(resultItemList);

        return returnVal;

    } // fromJson

    public PayloadEnvelope(T requestData, GobiiProcessType gobiiProcessType) {
        this.header.setGobiiProcessType(gobiiProcessType);
        this.payload.getData().add(requestData);
    }

    private Payload<T> payload = new Payload<>();

    Header header = new Header();

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Payload<T> getPayload() {
        return payload;
    }

    public void setPayload(Payload<T> payload) {
        this.payload = payload;
    }
}
