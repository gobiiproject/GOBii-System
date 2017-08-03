package org.gobiiproject.gobiibrapi.core.common;

import org.gobiiproject.gobiiapimodel.payload.Pagination;
import org.gobiiproject.gobiiapimodel.payload.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiMetaData {

    public BrapiMetaData() {

    }

    public BrapiMetaData(Pagination pagination, List<BrapiStatus> status, List<String> datafiles) {
        this.pagination = pagination;
        this.status = status;
        this.datafiles = datafiles;
    }

    Pagination pagination = new Pagination(0,0,0,0);
    List<BrapiStatus> status = new ArrayList<>();
    List<String> datafiles = new ArrayList<>();

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }


    // [{code: <code>, message: <message>}]
    public void addStatusMessage(String code, String message) {
        if( this.status == null ) {
            this.status = new ArrayList<>();
        }

        BrapiStatus brapiStatus = new BrapiStatus(code,message);

        this.status.add(brapiStatus);
    }

    public List<BrapiStatus> getStatus() {
        return status;
    }

    public void setStatus(List<BrapiStatus> status) {
        this.status = status;
    }

    public List<String> getDatafiles() {
        return datafiles;
    }

    public void setDatafiles(List<String> datafiles) {
        this.datafiles = datafiles;
    }
}
