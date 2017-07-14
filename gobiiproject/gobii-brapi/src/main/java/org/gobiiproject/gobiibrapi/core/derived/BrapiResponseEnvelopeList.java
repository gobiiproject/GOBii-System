package org.gobiiproject.gobiibrapi.core.derived;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiibrapi.core.common.BrapiMetaData;

/**
 * Created by Phil on 12/19/2016.
 */
public class BrapiResponseEnvelopeList<T_RESPONSE_TYPE_MASTER, T_RESPONSE_TYPE_DETAIL> {

    private Class<T_RESPONSE_TYPE_MASTER> brapiResponseTypeMaster;
    private Class<T_RESPONSE_TYPE_DETAIL> brapiResponseTypeDetail;

    public BrapiResponseEnvelopeList() {}

    public BrapiResponseEnvelopeList(Class<T_RESPONSE_TYPE_MASTER> brapiResponseTypeMaster,
                                     Class<T_RESPONSE_TYPE_DETAIL> brapiResponseTypeDetail) {

        this.brapiResponseTypeMaster = brapiResponseTypeMaster;
        this.brapiResponseTypeDetail = brapiResponseTypeDetail;
    }


    private BrapiMetaData brapiMetaData = new BrapiMetaData();

    private BrapiListResult<T_RESPONSE_TYPE_DETAIL> data;

    @JsonProperty("metadata")
    public BrapiMetaData getBrapiMetaData() {
        return brapiMetaData;
    }

    @JsonProperty("metadata")
    public void setBrapiMetaData(BrapiMetaData brapiMetaData) {
        this.brapiMetaData = brapiMetaData;
    }

    @JsonProperty("result")
    public BrapiListResult<T_RESPONSE_TYPE_DETAIL> getData() {
        return data;
    }

    @JsonProperty("result")
    public void setData(BrapiListResult<T_RESPONSE_TYPE_DETAIL> data) {
        this.data = data;
    }
}
