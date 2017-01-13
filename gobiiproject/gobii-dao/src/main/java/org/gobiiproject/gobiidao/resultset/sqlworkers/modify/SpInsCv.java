package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

/**
 * Created by Angel on 5/4/2016.
 */
public class SpInsCv extends SpDef {

    public SpInsCv() {

        super("{call createcv(?,?,?,?)}");

        this.addParamDef("group", String.class).setNullable(false);
        this.addParamDef("term", String.class).setNullable(false);
        this.addParamDef("definition", String.class).setNullable(false);
        this.addParamDef("rank", Integer.class).setNullable(false);


    } // ctor
}
