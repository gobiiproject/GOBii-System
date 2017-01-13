package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Angel on 5/5/2016.
 */
public class SpUpdDisplay extends SpDef {

    public SpUpdDisplay() {

        super("{call updatedisplay(?,?,?,?,?,?,?,?,?)}",false);

        this.addParamDef("displayId", Integer.class).setNullable(false);
        this.addParamDef("tableName", String.class).setNullable(false);
        this.addParamDef("columnName", String.class).setNullable(true);
        this.addParamDef("displayName", String.class).setNullable(true);
        this.addParamDef("createdBy", Integer.class).setNullable(true);
        this.addParamDef("createdDate", Date.class).setNullable(true);
        this.addParamDef("modifiedBy", Integer.class).setNullable(true);
        this.addParamDef("modifiedDate", Date.class).setNullable(true);
        this.addParamDef("displayRank", Integer.class).setNullable(false);

    } // ctor
}
