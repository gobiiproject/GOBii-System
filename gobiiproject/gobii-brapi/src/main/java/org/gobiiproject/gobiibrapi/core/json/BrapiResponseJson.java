package org.gobiiproject.gobiibrapi.core.json;

import com.google.gson.JsonObject;
import org.gobiiproject.gobiibrapi.core.common.BrapiMetaData;

import java.util.List;

/**
 * Created by Phil on 12/16/2016.
 */
public class BrapiResponseJson<T_LIST_ITEM> {

    BrapiMetaData brapiMetaData;
    JsonObject resultMasterJson;
    List<T_LIST_ITEM> data;

    public BrapiMetaData getBrapiMetaData() {
        return brapiMetaData;
    }

    public void setBrapiMetaData(BrapiMetaData brapiMetaData) {
        this.brapiMetaData = brapiMetaData;
    }

    public JsonObject getResultMasterJson() {
        return resultMasterJson;
    }

    public void setResultMasterJson(JsonObject resultMasterJson) {
        this.resultMasterJson = resultMasterJson;
    }

    public List<T_LIST_ITEM> getData() {
        return data;
    }

    public void setData(List<T_LIST_ITEM> data) {
        this.data = data;
    }
}
