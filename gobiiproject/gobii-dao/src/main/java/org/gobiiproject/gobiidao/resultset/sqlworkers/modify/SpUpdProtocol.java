package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.util.Date;

/**
 * Created by VCalaminos on 2016-12-13.
 */
public class SpUpdProtocol extends SpDef {

    public SpUpdProtocol() {

        super("{call updateprotocol(?,?,?,?,?,?,?,?,?,?)}", false);

        this.addParamDef("protocolId", Integer.class).setNullable(false);
        this.addParamDef("name", String.class).setNullable(false);
        this.addParamDef("description", String.class).setNullable(false);
        this.addParamDef("typeId", Integer.class).setNullable(true);
        this.addParamDef("platformId", Integer.class).setNullable(true);
        this.addParamDef("createdBy", Integer.class).setNullable(false);
        this.addParamDef("createdDate", Date.class).setNullable(false);
        this.addParamDef("modifiedBy", Integer.class).setNullable(false);
        this.addParamDef("modifiedDate", Date.class).setNullable(true);
        this.addParamDef("status", Integer.class).setNullable(false);
    }

}
