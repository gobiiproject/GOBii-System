package org.gobiiproject.gobiibrapi.core.derived;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiibrapi.core.common.BrapiMetaData;

/**
 * Created by Phil on 12/19/2016.
 */
public class BrapiResponseEnvelopeMaster<T_RESPONSE_TYPE_MASTER> {

    private Class<T_RESPONSE_TYPE_MASTER> brapiResponseTypeMaster;

    public BrapiResponseEnvelopeMaster() {}

    public BrapiResponseEnvelopeMaster(Class<T_RESPONSE_TYPE_MASTER> brapiResponseTypeMaster) {

        this.brapiResponseTypeMaster = brapiResponseTypeMaster;
    }

    private BrapiMetaData brapiMetaData = new BrapiMetaData();

    private T_RESPONSE_TYPE_MASTER result;

    @JsonProperty("metadata")
    public BrapiMetaData getBrapiMetaData() {
        return brapiMetaData;
    }

    @JsonProperty("metadata")
    public void setBrapiMetaData(BrapiMetaData brapiMetaData) {
        this.brapiMetaData = brapiMetaData;
    }

    @JsonProperty("result")
    public T_RESPONSE_TYPE_MASTER getResult() {
        return this.result;
    }

    @JsonProperty("result")
    public void setResult(T_RESPONSE_TYPE_MASTER result) {
        this.result = result;
    }
}
