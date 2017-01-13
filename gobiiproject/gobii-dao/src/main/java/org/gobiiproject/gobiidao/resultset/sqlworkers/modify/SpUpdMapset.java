package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.util.Date;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpUpdMapset extends SpDef {

    public SpUpdMapset() {


        super("{call updatemapset(?,?,?,?,?,?,?,?,?,?,?)}",false);

        this.addParamDef("mapsetId", Integer.class).setNullable(false);
        this.addParamDef("name", String.class).setNullable(false);
        this.addParamDef("code", String.class).setNullable(false);
        this.addParamDef("description", String.class).setNullable(true);
        this.addParamDef("referenceId", Integer.class).setNullable(true);
        this.addParamDef("mapType", Integer.class).setNullable(false);
        this.addParamDef("createdBy", Integer.class).setNullable(false);
        this.addParamDef("createdDate", java.util.Date.class).setNullable(false);
        this.addParamDef("modifiedBy", Integer.class).setNullable(true);
        this.addParamDef("modifiedDate", java.util.Date.class).setNullable(true);
        this.addParamDef("status", Integer.class).setNullable(false);

    } // ctor
}
