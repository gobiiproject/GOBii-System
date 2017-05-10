package org.gobiiproject.gobiibrapi.calls.calls;

import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiibrapi.types.BrapiDataTypes;
import org.gobiiproject.gobiimodel.types.RestMethodTypes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseMapCalls {

    String contextRoot = null;

    public BrapiResponseMapCalls(HttpServletRequest request) {

        this.contextRoot = request.getContextPath();
    }

    private List<BrapiResponseCallsItem> getBrapiResponseCallsItems() throws Exception {

        List<BrapiResponseCallsItem> returnVal = new ArrayList<>();

        returnVal.add(new BrapiResponseCallsItem(new RestUri(this.contextRoot,
                ControllerType.BRAPI,
                ServiceRequestId.URL_CALLS),
                Arrays.asList(RestMethodTypes.GET),
                Arrays.asList(BrapiDataTypes.JSON)));

        returnVal.add(new BrapiResponseCallsItem(new RestUri(this.contextRoot,
                ControllerType.BRAPI,
                ServiceRequestId.URL_STUDIES_SEARCH),
                Arrays.asList(RestMethodTypes.POST),
                Arrays.asList(BrapiDataTypes.JSON)));

        returnVal.add(new BrapiResponseCallsItem(new RestUri(this.contextRoot,
                ControllerType.BRAPI,
                ServiceRequestId.URL_GERMPLASM).addUriParam("id"),
                Arrays.asList(RestMethodTypes.POST),
                Arrays.asList(BrapiDataTypes.JSON)));

        returnVal.add(new BrapiResponseCallsItem(new RestUri(this.contextRoot,
                ControllerType.BRAPI,
                ServiceRequestId.URL_STUDIES).addUriParam("id").appendSegment(ServiceRequestId.URL_OBSERVATION_VARIABLES),
                Arrays.asList(RestMethodTypes.POST),
                Arrays.asList(BrapiDataTypes.JSON)));

        return returnVal;
    }

    public BrapiResponseCalls getBrapiResponseCalls() throws Exception {

        BrapiResponseCalls returnVal = new BrapiResponseCalls();
        List<BrapiResponseCallsItem> brapiResponseCallsItems = this.getBrapiResponseCallsItems();
        returnVal.setData(brapiResponseCallsItems);

        return returnVal;

    }
}
