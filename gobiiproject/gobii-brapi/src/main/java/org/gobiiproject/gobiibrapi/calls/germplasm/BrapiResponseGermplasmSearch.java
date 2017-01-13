package org.gobiiproject.gobiibrapi.calls.germplasm;

import org.gobiiproject.gobiibrapi.core.common.BrapiMetaData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 12/18/2016.
 */
public class BrapiResponseGermplasmSearch {

    private String typeOfGermplasmStorageCode;

    private String pedigree;

    private String seedSource;
    private String species;

    private String subtaxa;

    private String biologicalStatusOfAccessionCode;

    private String countryOfOriginCode;

    private List<String> synonyms;

    private String genus;

    private String instituteName;

    private String subtaxaAuthority;

    private String germplasmDbId;

    private String defaultDisplayName;

    private String acquisitionDate;

    private List<GermplasmDonor> donors = new ArrayList<>();

    private String accessionNumber;

    private String commonCropName;

    private String speciesAuthority;

    private String germplasmName;

    private String germplasmPUI;

    private String instituteCode;

    public String getTypeOfGermplasmStorageCode ()
    {
        return typeOfGermplasmStorageCode;
    }

    public void setTypeOfGermplasmStorageCode (String typeOfGermplasmStorageCode)
    {
        this.typeOfGermplasmStorageCode = typeOfGermplasmStorageCode;
    }

    public String getPedigree ()
    {
        return pedigree;
    }

    public void setPedigree (String pedigree)
    {
        this.pedigree = pedigree;
    }

    public String getSeedSource ()
    {
        return seedSource;
    }

    public void setSeedSource (String seedSource)
    {
        this.seedSource = seedSource;
    }

    public String getSpecies ()
    {
        return species;
    }

    public void setSpecies (String species)
    {
        this.species = species;
    }

    public String getSubtaxa ()
    {
        return subtaxa;
    }

    public void setSubtaxa (String subtaxa)
    {
        this.subtaxa = subtaxa;
    }

    public String getBiologicalStatusOfAccessionCode ()
    {
        return biologicalStatusOfAccessionCode;
    }

    public void setBiologicalStatusOfAccessionCode (String biologicalStatusOfAccessionCode)
    {
        this.biologicalStatusOfAccessionCode = biologicalStatusOfAccessionCode;
    }

    public String getCountryOfOriginCode ()
    {
        return countryOfOriginCode;
    }

    public void setCountryOfOriginCode (String countryOfOriginCode)
    {
        this.countryOfOriginCode = countryOfOriginCode;
    }

    public List<String> getSynonyms ()
    {
        return synonyms;
    }

    public void setSynonyms (List<String> synonyms)
    {
        this.synonyms = synonyms;
    }

    public String getGenus ()
    {
        return genus;
    }

    public void setGenus (String genus)
    {
        this.genus = genus;
    }

    public String getInstituteName ()
    {
        return instituteName;
    }

    public void setInstituteName (String instituteName)
    {
        this.instituteName = instituteName;
    }

    public String getSubtaxaAuthority ()
    {
        return subtaxaAuthority;
    }

    public void setSubtaxaAuthority (String subtaxaAuthority)
    {
        this.subtaxaAuthority = subtaxaAuthority;
    }

    public String getGermplasmDbId ()
    {
        return germplasmDbId;
    }

    public void setGermplasmDbId (String germplasmDbId)
    {
        this.germplasmDbId = germplasmDbId;
    }

    public String getDefaultDisplayName ()
    {
        return defaultDisplayName;
    }

    public void setDefaultDisplayName (String defaultDisplayName)
    {
        this.defaultDisplayName = defaultDisplayName;
    }

    public String getAcquisitionDate ()
    {
        return acquisitionDate;
    }

    public void setAcquisitionDate (String acquisitionDate)
    {
        this.acquisitionDate = acquisitionDate;
    }

    public List<GermplasmDonor> getDonors ()
    {
        return donors;
    }

    public void setDonors (List<GermplasmDonor> donors)
    {
        this.donors = donors;
    }

    public String getAccessionNumber ()
    {
        return accessionNumber;
    }

    public void setAccessionNumber (String accessionNumber)
    {
        this.accessionNumber = accessionNumber;
    }

    public String getCommonCropName ()
    {
        return commonCropName;
    }

    public void setCommonCropName (String commonCropName)
    {
        this.commonCropName = commonCropName;
    }

    public String getSpeciesAuthority ()
    {
        return speciesAuthority;
    }

    public void setSpeciesAuthority (String speciesAuthority)
    {
        this.speciesAuthority = speciesAuthority;
    }

    public String getGermplasmName ()
    {
        return germplasmName;
    }

    public void setGermplasmName (String germplasmName)
    {
        this.germplasmName = germplasmName;
    }

    public String getGermplasmPUI ()
    {
        return germplasmPUI;
    }

    public void setGermplasmPUI (String germplasmPUI)
    {
        this.germplasmPUI = germplasmPUI;
    }

    public String getInstituteCode ()
    {
        return instituteCode;
    }

    public void setInstituteCode (String instituteCode)
    {
        this.instituteCode = instituteCode;
    }

}
