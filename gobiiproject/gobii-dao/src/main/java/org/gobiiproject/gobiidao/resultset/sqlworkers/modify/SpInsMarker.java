package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.util.ArrayList;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpInsMarker extends SpDef {

    /*
      IN platformid integer,
    IN variantid integer,
    IN markername text,
    IN markercode text,
    IN markerref text,
    IN markeralts text[],
    IN markersequence text,
    IN referenceid integer,
    IN strandid integer,
    IN markerstatus integer,
    * */

    public SpInsMarker() {

        super("{call createmarker(?,?,?,?,?,?,?,?,?,?)}");

        this.addParamDef("platformId", Integer.class).setNullable(false);
        this.addParamDef("variantId", Integer.class).setNullable(true);
        this.addParamDef("markerName", String.class).setNullable(false);
        this.addParamDef("code", String.class).setNullable(true);
        this.addParamDef("ref", String.class).setNullable(true);
        this.addParamDef("alts", ArrayList.class).setNullable(true);
        this.addParamDef("sequence", String.class).setNullable(true);
        this.addParamDef("referenceId", Integer.class).setNullable(true);
        this.addParamDef("strandId", Integer.class).setNullable(true);
        this.addParamDef("status", Integer.class).setNullable(false);


    } // ctor

}
