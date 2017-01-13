package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.EntityPropertyParamNames;
import org.gobiiproject.gobiidao.resultset.core.SpDef;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpInsMapsetProperties extends SpDef {

    public SpInsMapsetProperties() {

        super("{call upsertmapsetpropertybyname(?,?,?)}");

        this.addParamDef(EntityPropertyParamNames.PROPPCOLARAMNAME_ENTITY_ID, Integer.class)
                .setNullable(false);

        this.addParamDef(EntityPropertyParamNames.PROPPCOLARAMNAME_NAME, String.class)
                .setNullable(false);

        this.addParamDef(EntityPropertyParamNames.PROPPCOLARAMNAME_VALUE, String.class)
                .setNullable(true);

    } // ctor
}
