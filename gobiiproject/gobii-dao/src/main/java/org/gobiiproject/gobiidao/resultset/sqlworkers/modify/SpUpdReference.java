package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.util.Date;

/**
 * Created by Angel on 5/5/2016.
 */
public class SpUpdReference extends SpDef {

    public SpUpdReference() {

        super("{call updatereference(?,?,?,?,?)}",false);

        this.addParamDef("referenceId",Integer.class).setNullable(true);
        this.addParamDef("name", String.class).setNullable(false);
        this.addParamDef("version", String.class).setNullable(false);
        this.addParamDef("link", String.class).setNullable(true);
        this.addParamDef("filePath", String.class).setNullable(true);
    } // ctor
}
