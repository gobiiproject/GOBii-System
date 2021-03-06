package org.gobiiproject.gobiibrapi.core.json;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiListResultJson<T_LIST_ITEM> {

    @JsonIgnore
    private Class<T_LIST_ITEM> listItemType;


    private List<T_LIST_ITEM> data;

    public BrapiListResultJson(Class<T_LIST_ITEM> listItemType) {
        this.listItemType = listItemType;
    }


    public List<T_LIST_ITEM> getData() {
        return data;
    }

    public void setData(List<T_LIST_ITEM> data) {
        this.data = data;
    }
}
