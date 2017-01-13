package org.gobiiproject.gobiiapimodel.hateos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 9/26/2016.
 */
/**
 * The indices of both link collections correspond to the indices in the
 * Payload's dataitems list. So the client will look up the link and allowable
 * methods for a given item in linksPerDataItem.
 * exploreLinksPerDataItem follows generally the same pattern but is more
 * open-ended for specific method calls to expand on because you can have more
 * than one link per data item. However, again, in _both_ cases, the indices
 * in these collections _must_ correspond to those in the data items list of the
 * payload. So, if there is not a link for a data item, ther emust be a null value
 * at that index in the link collection. Either or both collections may be allowed
 * to be empty depending on the specific call.
 */

public class LinkCollection {
    private List<Link> linksPerDataItem = new ArrayList<>();
    private List<List<Link>> exploreLinksPerDataItem = new ArrayList<>();

    public List<Link> getLinksPerDataItem() {
        return linksPerDataItem;
    }

    public void setLinksPerDataItem(List<Link> linksPerDataItem) {
        this.linksPerDataItem = linksPerDataItem;
    }

    public List<List<Link>> getExploreLinksPerDataItem() {
        return exploreLinksPerDataItem;
    }

    public void setExploreLinksPerDataItem(List<List<Link>> exploreLinksPerDataItem) {
        this.exploreLinksPerDataItem = exploreLinksPerDataItem;
    }
}
