package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

/**
 * Created by VCalaminos on 6/21/2017.
 */
public class SpUpdMarkerGroupName extends SpDef{

    public SpUpdMarkerGroupName() {

        super("{call updatemarkergroupname(?,?)}", false);

        this.addParamDef("markerGroupId", Integer.class).setNullable(false);
        this.addParamDef("name", String.class).setNullable(false);


    }




}
