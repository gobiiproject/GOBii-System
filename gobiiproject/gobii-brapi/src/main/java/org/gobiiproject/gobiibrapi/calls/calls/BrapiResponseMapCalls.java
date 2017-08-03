package org.gobiiproject.gobiibrapi.calls.calls;

import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
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
                GobiiControllerType.BRAPI.getControllerPath(),
                GobiiServiceRequestId.URL_CALLS.getResourcePath()),
                Arrays.asList(RestMethodTypes.GET),
                Arrays.asList(BrapiDataTypes.JSON)));

        returnVal.add(new BrapiResponseCallsItem(new RestUri(this.contextRoot,
                GobiiControllerType.BRAPI.getControllerPath(),
                GobiiServiceRequestId.URL_STUDIES_SEARCH.getResourcePath()),
                Arrays.asList(RestMethodTypes.POST),
                Arrays.asList(BrapiDataTypes.JSON)));

        returnVal.add(new BrapiResponseCallsItem(new RestUri(this.contextRoot,
                GobiiControllerType.BRAPI.getControllerPath(),
                GobiiServiceRequestId.URL_GERMPLASM.getResourcePath()).addUriParam("id"),
                Arrays.asList(RestMethodTypes.POST),
                Arrays.asList(BrapiDataTypes.JSON)));

        returnVal.add(new BrapiResponseCallsItem(new RestUri(this.contextRoot,
                GobiiControllerType.BRAPI.getControllerPath(),
                GobiiServiceRequestId.URL_STUDIES.getResourcePath()).addUriParam("id").appendSegment(GobiiServiceRequestId.URL_OBSERVATION_VARIABLES),
                Arrays.asList(RestMethodTypes.POST),
                Arrays.asList(BrapiDataTypes.JSON)));

        returnVal.add(new BrapiResponseCallsItem(new RestUri(this.contextRoot,
                GobiiControllerType.BRAPI.getControllerPath(),
                GobiiServiceRequestId.URL_ALLELE_MATRICES.getResourcePath()).addQueryParam("studyDbId"),
                Arrays.asList(RestMethodTypes.GET),
                Arrays.asList(BrapiDataTypes.JSON)));

        returnVal.add(new BrapiResponseCallsItem(new RestUri(this.contextRoot,
                GobiiControllerType.BRAPI.getControllerPath(),
                GobiiServiceRequestId.URL_ALLELE_MATRIX_SEARCH .getResourcePath()),
                Arrays.asList(RestMethodTypes.GET,RestMethodTypes.POST),
                Arrays.asList(BrapiDataTypes.FLAPJACK)));


        return returnVal;
    }

    public BrapiResponseCalls getBrapiResponseCalls() throws Exception {

        BrapiResponseCalls returnVal = new BrapiResponseCalls();
        List<BrapiResponseCallsItem> brapiResponseCallsItems = this.getBrapiResponseCallsItems();
        returnVal.setData(brapiResponseCallsItems);

        return returnVal;

    }
}
