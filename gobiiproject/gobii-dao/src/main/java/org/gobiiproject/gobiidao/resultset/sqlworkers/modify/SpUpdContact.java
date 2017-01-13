package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.util.ArrayList;

/**
 * Created by Angel on 5/5/2016.
 */
public class SpUpdContact extends SpDef {

    public SpUpdContact() {

        super("{call updatecontact(?,?,?,?,?,?,?,?,?,?,?)}",false);

        this.addParamDef("contactId", Integer.class).setNullable(false);
        this.addParamDef("lastName", String.class).setNullable(false);
        this.addParamDef("firstName", String.class).setNullable(false);
        this.addParamDef("code", String.class).setNullable(false);
        this.addParamDef("email", String.class).setNullable(false);
        this.addParamDef("roles", ArrayList.class).setNullable(true);
        this.addParamDef("createdBy", Integer.class).setNullable(false);
        this.addParamDef("createdDate", java.util.Date.class).setNullable(false);
        this.addParamDef("modifiedBy", Integer.class).setNullable(true);
        this.addParamDef("modifiedDate", java.util.Date.class).setNullable(true);
        this.addParamDef("organizationId", Integer.class).setNullable(true);


    } // ctor
}
