package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.util.Date;


/**
 * Created by Phil on 4/7/2016.
 */
public class SpInsProject extends SpDef {

    public SpInsProject() {

        super("{call createproject(?,?,?,?,?,?,?,?,?)}");

        this.addParamDef("projectName", String.class)
                .setNullable(false);

        this.addParamDef("projectCode",String.class)
                .setNullable(false);

        this.addParamDef("projectDescription",String.class)
                .setNullable(true);

        this.addParamDef("piContact", Integer.class)
                .setNullable(false);

        this.addParamDef("createdBy", Integer.class)
                .setNullable(false);

        this.addParamDef("createdDate",Date.class )
                .setNullable(false);

        this.addParamDef("modifiedBy", Integer.class)
                .setNullable(false);

        this.addParamDef("modifiedDate", Date.class)
                .setNullable(true);

        this.addParamDef("projectStatus", Integer.class)
                .setNullable(false);


    } // ctor

}
