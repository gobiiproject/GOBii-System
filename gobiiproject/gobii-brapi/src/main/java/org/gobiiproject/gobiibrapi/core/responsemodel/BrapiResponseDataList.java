package org.gobiiproject.gobiibrapi.core.responsemodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 5/1/2017.
 */
public class BrapiResponseDataList<T_CALL_RESPONSE_DETAIL> {

    private List<T_CALL_RESPONSE_DETAIL> data = new ArrayList<T_CALL_RESPONSE_DETAIL>();

    @JsonProperty("data")
    public List<T_CALL_RESPONSE_DETAIL> getData() {
        return this.data;
    }

    @JsonProperty("data")
    public void setData(List<T_CALL_RESPONSE_DETAIL> data) {
        this.data = data;
    }
}
