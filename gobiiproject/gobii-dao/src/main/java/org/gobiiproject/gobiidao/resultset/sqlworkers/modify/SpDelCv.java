package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

/**
 * Created by Angel on 5/5/2016.
 */
public class SpDelCv extends SpDef {

    public SpDelCv() {

        super("{call deletecv(?)}",false);

        this.addParamDef("cvId",Integer.class).setNullable(false);
    } // ctor
}
