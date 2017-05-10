package org.gobiiproject.gobiibrapi.core.responsemodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiibrapi.core.common.BrapiMetaData;

/**
 * Created by Phil on 5/1/2017.
 */
public class BrapResponseEnvelope {

    private BrapiMetaData brapiMetaData = new BrapiMetaData();
    @JsonProperty("metadata")
    public BrapiMetaData getBrapiMetaData() {
        return brapiMetaData;
    }

    @JsonProperty("metadata")
    public void setBrapiMetaData(BrapiMetaData brapiMetaData) {
        this.brapiMetaData = brapiMetaData;
    }
}
