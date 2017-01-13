package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Angel on 5/4/2016.
 */
public class SpInsReference extends SpDef {

    public SpInsReference() {

        super("{call createreference(?,?,?,?)}");

        this.addParamDef("name", String.class).setNullable(false);
        this.addParamDef("version", String.class).setNullable(false);
        this.addParamDef("link", String.class).setNullable(true);
        this.addParamDef("filePath", String.class).setNullable(true);


    } // ctor
}
