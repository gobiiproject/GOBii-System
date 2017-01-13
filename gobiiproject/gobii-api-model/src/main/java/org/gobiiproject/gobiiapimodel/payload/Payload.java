package org.gobiiproject.gobiiapimodel.payload;

import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 9/6/2016.
 */
public class Payload<T> {

    LinkCollection linkCollection = new LinkCollection();
    private List<T> data = new ArrayList<>();

    public List<T> getData() {
        return data;
    }
    public void setData(List<T> data) {
        this.data = data;
    }

    public LinkCollection getLinkCollection() {
        return linkCollection;
    }

    public void setLinkCollection(LinkCollection linkCollection) {
        this.linkCollection = linkCollection;
    }
}
