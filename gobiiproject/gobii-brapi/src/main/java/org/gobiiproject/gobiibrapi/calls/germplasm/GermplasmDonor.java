package org.gobiiproject.gobiibrapi.calls.germplasm;

/**
 * Created by Phil on 12/18/2016.
 */
public class GermplasmDonor {

    private String donorGermplasmPUI;

    private String donorAccessionNumber;

    private String donorInstituteCode;

    public GermplasmDonor() {}

    public GermplasmDonor(String donorGermplasmPUI, String donorAccessionNumber, String donorInstituteCode) {
        this.donorGermplasmPUI = donorGermplasmPUI;
        this.donorAccessionNumber = donorAccessionNumber;
        this.donorInstituteCode = donorInstituteCode;
    }

    public String getDonorGermplasmPUI ()
    {
        return donorGermplasmPUI;
    }

    public void setDonorGermplasmPUI (String donorGermplasmPUI)
    {
        this.donorGermplasmPUI = donorGermplasmPUI;
    }

    public String getDonorAccessionNumber ()
    {
        return donorAccessionNumber;
    }

    public void setDonorAccessionNumber (String donorAccessionNumber)
    {
        this.donorAccessionNumber = donorAccessionNumber;
    }

    public String getDonorInstituteCode ()
    {
        return donorInstituteCode;
    }

    public void setDonorInstituteCode (String donorInstituteCode)
    {
        this.donorInstituteCode = donorInstituteCode;
    }
}
