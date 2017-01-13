// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.core;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TypedRestRequest<T extends DtoMetaData> {


    private final Class<T> paramType;
    private HttpCore httpCore = null;
    Logger LOGGER = LoggerFactory.getLogger(TypedRestRequest.class);

    public TypedRestRequest(String baseUrl,
                            Integer port,
                            Class<T> paramType) {

        this.paramType = paramType;
        httpCore = new HttpCore(baseUrl, port);

    } // ctor


    public T getTypedHtppResponseForDto(String url,
                                        T dtoInstance,
                                        String token) throws Exception {

        T returnVal = null;


        if( ClientContext.isInitialized() ) {
            GobiiCropType gobiiCropType = ClientContext.getInstance(null, false).getCurrentClientCropType();
            dtoInstance.setGobiiCropType(gobiiCropType);
        }


        ObjectMapper objectMapper = new ObjectMapper();
        String dtoRequestJson = objectMapper.writeValueAsString(dtoInstance);
        JsonObject responseJson = httpCore.getResponseBody(url, dtoRequestJson, token);

        returnVal = objectMapper.readValue(responseJson.toString(), paramType);

        return returnVal;


    } // getTypedHtppResponseForDto()


}// ArgumentDAOTest
