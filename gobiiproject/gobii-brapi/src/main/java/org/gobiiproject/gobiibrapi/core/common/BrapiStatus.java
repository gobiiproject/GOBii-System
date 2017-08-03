package org.gobiiproject.gobiibrapi.core.common;

import org.gobiiproject.gobiiapimodel.payload.Pagination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiStatus {

    public BrapiStatus() {

    }

    public BrapiStatus(String code, String message ) {

        this.code = code;
        this.message = message;
    }

    String code;
    String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
