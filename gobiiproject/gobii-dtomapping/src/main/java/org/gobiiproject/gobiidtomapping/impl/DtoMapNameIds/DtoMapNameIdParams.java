package org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds;

import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;

/**
 * Created by Phil on 10/16/2016.
 */
public class DtoMapNameIdParams {

    private GobiiEntityNameType gobiiEntityNameType;
    private GobiiFilterType gobiiFilterType;
    private Object filterValue;

    public DtoMapNameIdParams(GobiiEntityNameType gobiiEntityNameType, GobiiFilterType gobiiFilterType, Object filterValue) {
        this.gobiiEntityNameType = gobiiEntityNameType;
        this.gobiiFilterType = gobiiFilterType;
        this.filterValue = filterValue;
    }

    public GobiiEntityNameType getEntityType() {
        return gobiiEntityNameType;
    }

    public void setEntity(GobiiEntityNameType entity) {
        this.gobiiEntityNameType = entity;
    }

    public GobiiFilterType getGobiiFilterType() {
        return gobiiFilterType;
    }

    public void setGobiiFilterType(GobiiFilterType gobiiFilterType) {
        this.gobiiFilterType = gobiiFilterType;
    }

    public Object getFilterValue() {
        return filterValue;
    }

    public Integer getFilterValueAsInteger() {
        return ((Integer) filterValue);
    }

    public String getFilterValueAsString() {
        return (filterValue.toString());
    }

    public void setFilterValue(Object filterValue) {
        this.filterValue = filterValue;
    }
}
