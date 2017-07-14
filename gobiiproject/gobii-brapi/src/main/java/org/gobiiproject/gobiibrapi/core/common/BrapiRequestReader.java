package org.gobiiproject.gobiibrapi.core.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiRequestReader<T_POST_OBJ> {


    Class<T_POST_OBJ> postObjType;


    private ObjectMapper objectMapper = new ObjectMapper();

    public BrapiRequestReader(Class<T_POST_OBJ> postObjType) {
        this.postObjType = postObjType;
    }


    public T_POST_OBJ makeRequestObj(String postBody) throws Exception {


        T_POST_OBJ returnVal = objectMapper.readValue(postBody, this.postObjType);


        return returnVal;

    }
}
