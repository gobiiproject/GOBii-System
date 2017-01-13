package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.util.Date;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpInsMarkerGroup extends SpDef {

    public SpInsMarkerGroup() {

        super("{call createmarkergroup(?,?,?,?,?,?,?,?)}");

        this.addParamDef("name",String.class).setNullable(false);
        this.addParamDef("code",String.class).setNullable(false);
        this.addParamDef("germplasmGroup",String.class).setNullable(true);
        this.addParamDef("createdBy", Integer.class).setNullable(true);
        this.addParamDef("createdDate", java.util.Date.class).setNullable(true);
        this.addParamDef("modifiedBy", Integer.class).setNullable(true);
        this.addParamDef("modifiedDate", java.util.Date.class).setNullable(true);
        this.addParamDef("status", Integer.class).setNullable(true);


    } // ctor
}
