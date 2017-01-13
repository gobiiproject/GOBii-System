package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.EntityPropertyParamNames;
import org.gobiiproject.gobiidao.resultset.core.SpDef;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;

import java.sql.Date;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpInsProjectProperties extends SpDef {

    public SpInsProjectProperties() {

        super("{call upsertprojectpropertybyname(?,?,?)}");

        this.addParamDef(EntityPropertyParamNames.PROPPCOLARAMNAME_ENTITY_ID, Integer.class)
                .setNullable(false);

        this.addParamDef(EntityPropertyParamNames.PROPPCOLARAMNAME_NAME,String.class)
                .setNullable(false);

        this.addParamDef(EntityPropertyParamNames.PROPPCOLARAMNAME_VALUE,String.class)
                .setNullable(true);

    } // ctor
}
