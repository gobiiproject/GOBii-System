package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpInsPlatform extends SpDef {

    public SpInsPlatform() {

        super("{call createplatform(?,?,?,?,?,?,?,?,?,?)}");

        this.addParamDef("platformName", String.class).setNullable(false);
        this.addParamDef("platformCode", String.class).setNullable(false);
        this.addParamDef("platformVendor", Integer.class).setNullable(true);
        this.addParamDef("platformDescription", String.class).setNullable(true);
        this.addParamDef("createdBy", Integer.class).setNullable(false);
        this.addParamDef("createdDate", java.util.Date.class).setNullable(false);
        this.addParamDef("modifiedBy", Integer.class).setNullable(true);
        this.addParamDef("modifiedDate", java.util.Date.class).setNullable(true);
        this.addParamDef("status", Integer.class).setNullable(false);
        this.addParamDef("typeId", Integer.class).setNullable(false);

    } // ctor
}
