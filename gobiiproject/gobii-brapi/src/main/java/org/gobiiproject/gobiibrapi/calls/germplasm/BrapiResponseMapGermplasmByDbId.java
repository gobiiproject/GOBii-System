package org.gobiiproject.gobiibrapi.calls.germplasm;

/**
 * Created by Phil on 12/18/2016.
 */
public class BrapiResponseMapGermplasmByDbId {


    public BrapiResponseGermplasmByDbId getGermplasmByDbid(Integer studyDbId ) throws Exception {
        BrapiResponseGermplasmByDbId returnVal = new BrapiResponseGermplasmByDbId();

        returnVal.setGermplasmDbId(studyDbId.toString());
        returnVal.setGermplasmName("test_germplasm_name");

        returnVal.getDonors().add(new GermplasmDonor("testdonorid1","testdoneraccessionnumber1","testdonorcode1"));
        returnVal.getDonors().add(new GermplasmDonor("testdonorid2","testdoneraccessionnumber2","testdonorcode2"));

        return returnVal;
    }


}
