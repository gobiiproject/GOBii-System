package org.gobiiproject.gobiiclient.core.brapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiibrapi.core.json.BrapiJsonKeys;
import org.gobiiproject.gobiibrapi.core.common.BrapiMetaData;
import org.gobiiproject.gobiibrapi.core.json.BrapiResponseJson;
import org.gobiiproject.gobiiclient.core.common.HttpMethodResult;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;

import java.util.List;

/**
 * Created by Phil on 12/16/2016.
 */
public class BrapiResourceJson<T_POST_OBJ_TYPE, T_RESPONSE_OBJ_DATA_LIST> {

    private RestUri restUri;
    private ObjectMapper objectMapper = new ObjectMapper();

    private Class<T_POST_OBJ_TYPE> brapiPostObjType;
    private Class<T_RESPONSE_OBJ_DATA_LIST> brapiResponsebjType;

    public BrapiResourceJson(RestUri restUri,
                             Class<T_POST_OBJ_TYPE> brapiPostObjType,
                             Class<T_RESPONSE_OBJ_DATA_LIST> brapiResponseObjType) {

        this.restUri = restUri;
        this.brapiPostObjType = brapiPostObjType;
        this.brapiResponsebjType = brapiResponseObjType;
    }

    private BrapiResponseJson<T_RESPONSE_OBJ_DATA_LIST> extractResponse(HttpMethodResult httpMethodResult) throws Exception {

        BrapiResponseJson<T_RESPONSE_OBJ_DATA_LIST> returnVal = new BrapiResponseJson<>();

        String brapiMetaDataString = httpMethodResult.getJsonPayload().get(BrapiJsonKeys.METADATA).toString();
        BrapiMetaData brapiMetaData = objectMapper.readValue(brapiMetaDataString, BrapiMetaData.class);
        returnVal.setBrapiMetaData(brapiMetaData);

        JsonObject resultAsJson = httpMethodResult.getJsonPayload().get(BrapiJsonKeys.RESULT).getAsJsonObject();
        JsonArray jsonArray = resultAsJson.get(BrapiJsonKeys.RESULT_DATA).getAsJsonArray();
        String arrayAsString = jsonArray.toString();
        List<T_RESPONSE_OBJ_DATA_LIST> resultItemList = objectMapper.readValue(arrayAsString,
                objectMapper.getTypeFactory().constructCollectionType(List.class, this.brapiResponsebjType));
        returnVal.setData(resultItemList);

        JsonObject resultWithoutDataList = resultAsJson.remove(BrapiJsonKeys.RESULT_DATA).getAsJsonObject();
        returnVal.setResultMasterJson(resultWithoutDataList);

        return returnVal;
    }


    public BrapiResponseJson<T_RESPONSE_OBJ_DATA_LIST> get() throws Exception {

        BrapiResponseJson<T_RESPONSE_OBJ_DATA_LIST> returnVal;

        HttpMethodResult httpMethodResult =
                GobiiClientContext.getInstance(null, false).getHttp()
                        .get(this.restUri);

        returnVal = this.extractResponse(httpMethodResult);

        return returnVal;
    }

    public BrapiResponseJson<T_RESPONSE_OBJ_DATA_LIST> post(T_POST_OBJ_TYPE bodyObj) throws Exception {


        BrapiResponseJson<T_RESPONSE_OBJ_DATA_LIST> returnVal;

        String bodyAsString = objectMapper.writeValueAsString(bodyObj);

        HttpMethodResult httpMethodResult =
                GobiiClientContext.getInstance(null, false).getHttp()
                        .post(this.restUri,
                                bodyAsString);

        returnVal = this.extractResponse(httpMethodResult);

        return returnVal;
    } //

}
