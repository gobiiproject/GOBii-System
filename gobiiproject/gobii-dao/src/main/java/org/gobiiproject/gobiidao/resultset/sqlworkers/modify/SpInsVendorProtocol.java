package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

/**
 * Created by VCalaminos on 2016-12-12.
 */
public class SpInsVendorProtocol extends SpDef {

    public SpInsVendorProtocol() {

        super("{call createvendorprotocol(?,?,?,?)}");

        this.addParamDef("name", String.class).setNullable(true);
        this.addParamDef("vendorId", Integer.class).setNullable(false);
        this.addParamDef("protocolId", Integer.class).setNullable(false);
        this.addParamDef("status", Integer.class).setNullable(true);

    }

}
