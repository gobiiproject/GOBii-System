package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.util.Date;

/**
 * Created by Angel on 5/4/2016.
 */
public class SpInsOrganization extends SpDef {

    public SpInsOrganization() {

        super("{call createorganization(?,?,?,?,?,?,?)}");

        this.addParamDef("name", String.class).setNullable(false);
        this.addParamDef("address", String.class).setNullable(false);
        this.addParamDef("website", String.class).setNullable(true);
        this.addParamDef("createdBy", Integer.class).setNullable(true);
        this.addParamDef("createdDate", Date.class).setNullable(true);
        this.addParamDef("modifiedBy", Integer.class).setNullable(true);
        this.addParamDef("modifiedDate", Date.class).setNullable(true);


    } // ctor
}
