package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.util.Date;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpUpdAnalysis extends SpDef {

    public SpUpdAnalysis() {

        super("{call updateanalysis(?,?,?,?,?,?,?,?,?,?,?,?,?)}",false);

        this.addParamDef("analysisId",Integer.class).setNullable(true);
        this.addParamDef("analysisName",String.class).setNullable(true);
        this.addParamDef("analysisDescription",String.class).setNullable(true);
        this.addParamDef("analysisTypeId",Integer.class).setNullable(true);
        this.addParamDef("program",String.class).setNullable(true);
        this.addParamDef("programVersion",String.class).setNullable(true);
        this.addParamDef("algorithm",String.class).setNullable(true);
        this.addParamDef("sourceName",String.class).setNullable(true);
        this.addParamDef("sourceVersion",String.class).setNullable(true);
        this.addParamDef("sourceUri",String.class).setNullable(true);
        this.addParamDef("referenceId",Integer.class).setNullable(true);
        this.addParamDef("timeExecuted",Date.class).setNullable(true);
        this.addParamDef("status",Integer.class).setNullable(true);

    } // ctor
}
