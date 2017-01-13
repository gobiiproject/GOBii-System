package org.gobiiproject.gobiibrapi.core.common;

import org.gobiiproject.gobiimodel.tobemovedtoapimodel.Pagination;

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

    public BrapiMetaData(Pagination pagination, List<Map<String, String>> status, List<String> datafiles) {
        this.pagination = pagination;
        this.status = status;
        this.datafiles = datafiles;
    }

    Pagination pagination = new Pagination(0,0,0,0);
    List<Map<String,String>> status = new ArrayList<>();
    List<String> datafiles = new ArrayList<>();

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public void addStatusMessage(String code, String message) {
        if( this.status == null ) {
            this.status = new ArrayList<>();
        }

        Map<String,String> statusItem = new HashMap<>();
        statusItem.put(code,message);
        this.status.add(statusItem);
    }

    public List<Map<String, String>> getStatus() {
        return status;
    }

    public void setStatus(List<Map<String, String>> status) {
        this.status = status;
    }

    public List<String> getDatafiles() {
        return datafiles;
    }

    public void setDatafiles(List<String> datafiles) {
        this.datafiles = datafiles;
    }
}
