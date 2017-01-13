package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.util.Date;

/**
 * Created by Angel on 5/4/2016.
 */
public class SpUpdOrganization extends SpDef {

    public SpUpdOrganization() {

        super("{call updateorganization(?,?,?,?,?,?,?,?)}",false);

        this.addParamDef("manifestId", Integer.class).setNullable(false);
        this.addParamDef("address", String.class).setNullable(false);
        this.addParamDef("website", String.class).setNullable(false);
        this.addParamDef("filePath", String.class).setNullable(true);
        this.addParamDef("createdBy", Integer.class).setNullable(true);
        this.addParamDef("createdDate", Date.class).setNullable(true);
        this.addParamDef("modifiedBy", Integer.class).setNullable(true);
        this.addParamDef("modifiedDate", Date.class).setNullable(true);


    } // ctor
}
