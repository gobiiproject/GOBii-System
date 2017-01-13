package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpInsExperiment extends SpDef {

    public SpInsExperiment() {

        super("{call createexperiment(?,?,?,?,?,?,?,?,?,?,?)}");

        this.addParamDef("experimentName", String.class).setNullable(false);
        this.addParamDef("experimentCode", String.class).setNullable(false);
        this.addParamDef("projectId", Integer.class).setNullable(false);
        this.addParamDef("platformId", Integer.class).setNullable(false);
        this.addParamDef("manifestId", Integer.class).setNullable(true);
        this.addParamDef("experimentDataFile", String.class).setNullable(true);
        this.addParamDef("createdBy", Integer.class).setNullable(false);
        this.addParamDef("createDate", Date.class).setNullable(false);
        this.addParamDef("modifiedBy", Integer.class).setNullable(false);
        this.addParamDef("modifiedDate", Date.class).setNullable(true);
        this.addParamDef("status", Integer.class).setNullable(false);


    } // ctor


}
