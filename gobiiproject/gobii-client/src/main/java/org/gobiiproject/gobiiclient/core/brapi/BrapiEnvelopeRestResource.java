package org.gobiiproject.gobiiclient.core.brapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiibrapi.core.common.BrapiMetaData;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelope;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseDataList;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMasterDetail;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMaster;
import org.gobiiproject.gobiibrapi.core.json.BrapiJsonKeys;
import org.gobiiproject.gobiiclient.core.common.HttpMethodResult;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;

/**
 * Created by Phil on 12/16/2016.
 */
public class BrapiEnvelopeRestResource<T_POST_OBJ_TYPE, T_RESPONSE_TYPE_MASTER, T_RESPONSE_TYPE_DETAIL extends BrapiResponseDataList> {

    private RestUri restUri;
    private ObjectMapper objectMapper = new ObjectMapper();

    private Class<T_POST_OBJ_TYPE> brapiPostObjType;
    private Class<T_RESPONSE_TYPE_MASTER> brapiResponseTypeMaster;
    private Class<T_RESPONSE_TYPE_DETAIL> brapiResponseTypeDetail;

    public BrapiEnvelopeRestResource(RestUri restUri,
                                     Class<T_POST_OBJ_TYPE> brapiPostObjType,
                                     Class<T_RESPONSE_TYPE_MASTER> brapiResponseTypeMaster,
                                     Class<T_RESPONSE_TYPE_DETAIL> brapiResponseTypeDetail) {

        this.restUri = restUri;
        this.brapiPostObjType = brapiPostObjType;
        this.brapiResponseTypeMaster = brapiResponseTypeMaster;
        this.brapiResponseTypeDetail = brapiResponseTypeDetail;
    }

    private BrapiResponseEnvelopeMasterDetail<T_RESPONSE_TYPE_DETAIL> getTypedListObjFromResult(HttpMethodResult httpMethodResult) throws Exception {

        BrapiResponseEnvelopeMasterDetail<T_RESPONSE_TYPE_DETAIL> returnVal = new BrapiResponseEnvelopeMasterDetail();


        String metaDataAsString = httpMethodResult.getJsonPayload().get(BrapiJsonKeys.METADATA).toString();
        BrapiMetaData brapiMetaData = objectMapper.readValue(metaDataAsString, BrapiMetaData.class);
        returnVal.setBrapiMetaData(brapiMetaData);

        String resultAsString = httpMethodResult.getJsonPayload().get(BrapiJsonKeys.RESULT).toString();
        T_RESPONSE_TYPE_DETAIL result = objectMapper.readValue(resultAsString, this.brapiResponseTypeDetail);
        returnVal.setResult(result);

//        String responseAsString = httpMethodResult.getJsonPayload().toString();
//        returnVal = objectMapper.readValue(responseAsString, BrapiResponseEnvelopeMasterDetail.class);

        return returnVal;
    }

    private BrapiResponseEnvelopeMaster<T_RESPONSE_TYPE_MASTER> getMasterObjFromResult(HttpMethodResult httpMethodResult) throws Exception {

        BrapiResponseEnvelopeMaster<T_RESPONSE_TYPE_MASTER> returnVal = new BrapiResponseEnvelopeMaster<>();


        String metaDataAsString = httpMethodResult.getJsonPayload().get(BrapiJsonKeys.METADATA).toString();
        BrapiMetaData brapiMetaData = objectMapper.readValue(metaDataAsString, BrapiMetaData.class);
        returnVal.setBrapiMetaData(brapiMetaData);

        String resultAsString = httpMethodResult.getJsonPayload().get(BrapiJsonKeys.RESULT).toString();
        T_RESPONSE_TYPE_MASTER result = objectMapper.readValue(resultAsString, this.brapiResponseTypeMaster);
        returnVal.setResult(result);


        return returnVal;
    }

    public BrapiResponseEnvelopeMasterDetail<T_RESPONSE_TYPE_DETAIL> getFromListResource() throws Exception {


        BrapiResponseEnvelopeMasterDetail<T_RESPONSE_TYPE_DETAIL> returnVal;

        HttpMethodResult httpMethodResult =
                GobiiClientContext.getInstance(null, false)
                        .getHttp()
                        .get(this.restUri);


        returnVal = this.getTypedListObjFromResult(httpMethodResult);

        return returnVal;

    } //


    public BrapiResponseEnvelopeMasterDetail<T_RESPONSE_TYPE_DETAIL> postToListResource(T_POST_OBJ_TYPE bodyObj) throws Exception {


        BrapiResponseEnvelopeMasterDetail<T_RESPONSE_TYPE_DETAIL> returnVal;

        String bodyAsString = objectMapper.writeValueAsString(bodyObj);

        HttpMethodResult httpMethodResult =
                GobiiClientContext.getInstance(null, false).getHttp()
                        .post(this.restUri,
                                bodyAsString);


        returnVal = this.getTypedListObjFromResult(httpMethodResult);

        return returnVal;

    } //

    public BrapiResponseEnvelopeMaster<T_RESPONSE_TYPE_MASTER> getFromMasterResource() throws Exception {

        BrapiResponseEnvelopeMaster<T_RESPONSE_TYPE_MASTER> returnVal;

        HttpMethodResult httpMethodResult =
                GobiiClientContext.getInstance(null, false).getHttp()
                        .get(this.restUri);

        returnVal = this.getMasterObjFromResult(httpMethodResult);

        return returnVal;
    }


    private BrapiMetaData getMetaDataFromResponse(HttpMethodResult httpMethodResult) throws Exception{

        String metaDataAsString = httpMethodResult.getJsonPayload().get(BrapiJsonKeys.METADATA).toString();
        BrapiMetaData returnVal = objectMapper.readValue(metaDataAsString, BrapiMetaData.class);

        return returnVal;

    }

    public BrapiResponseEnvelope getMetaDataResponse() throws Exception {

        BrapiResponseEnvelope returnVal = new BrapiResponseEnvelope();

        HttpMethodResult httpMethodResult =
                GobiiClientContext.getInstance(null, false).getHttp()
                        .get(this.restUri);

        BrapiMetaData brapiMetaData = this.getMetaDataFromResponse(httpMethodResult);
        returnVal.setBrapiMetaData(brapiMetaData);

        return returnVal;
    }

    // post request with request params only -- no body
    public BrapiResponseEnvelope posttQueryRequest() throws Exception {

        BrapiResponseEnvelope returnVal = new BrapiResponseEnvelope();

        HttpMethodResult httpMethodResult =
                GobiiClientContext.getInstance(null, false).getHttp()
                        .post(this.restUri, null);

        BrapiMetaData brapiMetaData = this.getMetaDataFromResponse(httpMethodResult);
        returnVal.setBrapiMetaData(brapiMetaData);

        return returnVal;

    }

}
