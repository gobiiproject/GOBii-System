package org.gobiiproject.gobiibrapi.calls.calls;

import org.gobiiproject.gobiiapimodel.restresources.ResourceBuilder;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiibrapi.core.derived.BrapiListResult;
import org.gobiiproject.gobiibrapi.core.derived.BrapiResponseEnvelopeList;
import org.gobiiproject.gobiibrapi.core.derived.BrapiResponseEnvelopeMaster;
import org.gobiiproject.gobiibrapi.types.BrapiDataTypes;
import org.gobiiproject.gobiimodel.types.RestMethodTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseMapCalls {

    public List<BrapiResponseCallsItem> getBrapiResponseCallsItems() throws Exception {

        List<BrapiResponseCallsItem> returnVal = new ArrayList<>();

        returnVal.add(new BrapiResponseCallsItem(ResourceBuilder.getUrlSegment(ServiceRequestId.URL_CALLS),
                Arrays.asList(RestMethodTypes.GET),
                Arrays.asList(BrapiDataTypes.JSON)));

        returnVal.add(new BrapiResponseCallsItem(ResourceBuilder.getUrlSegment(ServiceRequestId.URL_STUDIES_SEARCH),
                Arrays.asList(RestMethodTypes.POST),
                Arrays.asList(BrapiDataTypes.JSON)));

        return returnVal;
    }

    public BrapiListResult<BrapiResponseCallsItem> getBrapiResponseListCalls() throws Exception {

        BrapiListResult<BrapiResponseCallsItem> returnVal = new BrapiListResult<>(BrapiResponseCallsItem.class);
        returnVal.setData(this.getBrapiResponseCallsItems());
        return returnVal;

    }
}
