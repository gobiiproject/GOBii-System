package org.gobiiproject.gobiiclient.core.brapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiibrapi.core.common.BrapiMetaData;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseDataList;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMasterDetail;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMaster;
import org.gobiiproject.gobiibrapi.core.json.BrapiJsonKeys;
import org.gobiiproject.gobiiclient.core.common.HttpMethodResult;
import org.gobiiproject.gobiiclient.core.common.RestResourceUtils;

/**
 * Created by Phil on 12/16/2016.
 */
public class BrapiEnvelopeRestResource<T_POST_OBJ_TYPE, T_RESPONSE_TYPE_MASTER, T_RESPONSE_TYPE_DETAIL extends BrapiResponseDataList> {

    private RestUri restUri;
    private ObjectMapper objectMapper = new ObjectMapper();
    private RestResourceUtils restResourceUtils;

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
        this.restResourceUtils = new RestResourceUtils();
    }

    private BrapiResponseEnvelopeMasterDetail<T_RESPONSE_TYPE_DETAIL> getTypedListObjFromResult(HttpMethodResult httpMethodResult) throws Exception {

        BrapiResponseEnvelopeMasterDetail<T_RESPONSE_TYPE_DETAIL> returnVal = new BrapiResponseEnvelopeMasterDetail();


        String metaDataAsString = httpMethodResult.getPayLoad().get(BrapiJsonKeys.METADATA).toString();
        BrapiMetaData brapiMetaData = objectMapper.readValue(metaDataAsString, BrapiMetaData.class);
        returnVal.setBrapiMetaData(brapiMetaData);

        String resultAsString = httpMethodResult.getPayLoad().get(BrapiJsonKeys.RESULT).toString();
        T_RESPONSE_TYPE_DETAIL result = objectMapper.readValue(resultAsString, this.brapiResponseTypeDetail);
        returnVal.setResult(result);

//        String responseAsString = httpMethodResult.getPayLoad().toString();
//        returnVal = objectMapper.readValue(responseAsString, BrapiResponseEnvelopeMasterDetail.class);

        return returnVal;
    }

    private BrapiResponseEnvelopeMaster<T_RESPONSE_TYPE_MASTER> getMasterObjFromResult(HttpMethodResult httpMethodResult) throws Exception {

        BrapiResponseEnvelopeMaster<T_RESPONSE_TYPE_MASTER> returnVal = new BrapiResponseEnvelopeMaster<>();


        String metaDataAsString = httpMethodResult.getPayLoad().get(BrapiJsonKeys.METADATA).toString();
        BrapiMetaData brapiMetaData = objectMapper.readValue(metaDataAsString, BrapiMetaData.class);
        returnVal.setBrapiMetaData(brapiMetaData);

        String resultAsString = httpMethodResult.getPayLoad().get(BrapiJsonKeys.RESULT).toString();
        T_RESPONSE_TYPE_MASTER result = objectMapper.readValue(resultAsString, this.brapiResponseTypeMaster);
        returnVal.setResult(result);


        return returnVal;
    }

    public BrapiResponseEnvelopeMasterDetail<T_RESPONSE_TYPE_DETAIL> getFromListResource() throws Exception {


        BrapiResponseEnvelopeMasterDetail<T_RESPONSE_TYPE_DETAIL> returnVal;

        HttpMethodResult httpMethodResult =
                this.restResourceUtils.getClientContext().getHttp()
                        .get(this.restUri,
                                restResourceUtils.getClientContext().getUserToken());


        returnVal = this.getTypedListObjFromResult(httpMethodResult);

        return returnVal;

    } //


    public BrapiResponseEnvelopeMasterDetail<T_RESPONSE_TYPE_DETAIL> postToListResource(T_POST_OBJ_TYPE bodyObj) throws Exception {


        BrapiResponseEnvelopeMasterDetail<T_RESPONSE_TYPE_DETAIL> returnVal;

        String bodyAsString = objectMapper.writeValueAsString(bodyObj);

        HttpMethodResult httpMethodResult =
                this.restResourceUtils.getClientContext().getHttp()
                        .post(this.restUri,
                                bodyAsString,
                                restResourceUtils.getClientContext().getUserToken());


        returnVal = this.getTypedListObjFromResult(httpMethodResult);

        return returnVal;

    } //

    public BrapiResponseEnvelopeMaster<T_RESPONSE_TYPE_MASTER> getFromMasterResource() throws Exception {

        BrapiResponseEnvelopeMaster<T_RESPONSE_TYPE_MASTER> returnVal;

        HttpMethodResult httpMethodResult =
                this.restResourceUtils.getClientContext().getHttp()
                        .get(this.restUri,
                                restResourceUtils.getClientContext().getUserToken());

        returnVal = this.getMasterObjFromResult(httpMethodResult);

        return returnVal;
    }

}
