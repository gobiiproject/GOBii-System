package org.gobiiproject.gobiidao.resultset.sqlworkers;

import org.gobiiproject.gobiidao.resultset.core.EntityPropertyParamNames;
import org.gobiiproject.gobiidao.resultset.core.SpDef;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpInsPlatformProperties extends SpDef {

    public SpInsPlatformProperties() {

        super("{call upsertplatformpropertybyname(?,?,?)}",false);

        this.addParamDef(EntityPropertyParamNames.PROPPCOLARAMNAME_ENTITY_ID, Integer.class)
                .setNullable(false);

        this.addParamDef(EntityPropertyParamNames.PROPPCOLARAMNAME_NAME,String.class)
                .setNullable(false);

        this.addParamDef(EntityPropertyParamNames.PROPPCOLARAMNAME_VALUE,String.class)
                .setNullable(true);

    } // ctor
}
