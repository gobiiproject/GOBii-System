package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.EntityPropertyParamNames;
import org.gobiiproject.gobiidao.resultset.core.SpDef;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpDelMarkerGroupMarkerById extends SpDef {

    public SpDelMarkerGroupMarkerById() {

        super("{call deletemarkerinmarkergroupbyid(?,?)}",false);

        this.addParamDef(EntityPropertyParamNames.PROPPCOLARAMNAME_ENTITY_ID, Integer.class)
                .setNullable(false);

        this.addParamDef(EntityPropertyParamNames.PROPPCOLARAMNAME_PROP_ID, Integer.class)
                .setNullable(false);

    } // ctor
}
