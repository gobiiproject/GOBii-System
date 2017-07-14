package org.gobiiproject.gobiibrapi.calls.studies.observationvariables;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Phil on 12/18/2016.
 */
public class ObservationTrait {

    public ObservationTrait() {}

    public ObservationTrait(String name, String traitDbId) {
        this.name = name;
        this.traitDbId = traitDbId;
    }

    private String traitDbId;

    private String status;

    private String description;

    private String entity;

    private String name;

    private String xref;

    private String mainAbbreviation;

    private String attribute;

    private List<String> synonyms;

    private String traitclass;

    private List<String> alternativeAbbreviations;

    public String getTraitDbId ()
    {
        return traitDbId;
    }

    public void setTraitDbId (String traitDbId)
    {
        this.traitDbId = traitDbId;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getEntity ()
    {
        return entity;
    }

    public void setEntity (String entity)
    {
        this.entity = entity;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getXref ()
    {
        return xref;
    }

    public void setXref (String xref)
    {
        this.xref = xref;
    }

    public String getMainAbbreviation ()
    {
        return mainAbbreviation;
    }

    public void setMainAbbreviation (String mainAbbreviation)
    {
        this.mainAbbreviation = mainAbbreviation;
    }

    public String getAttribute ()
    {
        return attribute;
    }

    public void setAttribute (String attribute)
    {
        this.attribute = attribute;
    }

    public List<String> getSynonyms ()
    {
        return synonyms;
    }

    public void setSynonyms (List<String> synonyms)
    {
        this.synonyms = synonyms;
    }

    @JsonProperty("class")
    public String getTraitClass ()
    {
        return traitclass;
    }

    @JsonProperty("class")
    public void setTraitClass (String traitclass)
    {
        this.traitclass = traitclass;
    }

    public List<String> getAlternativeAbbreviations ()
    {
        return alternativeAbbreviations;
    }

    public void setAlternativeAbbreviations (List<String> alternativeAbbreviations)
    {
        this.alternativeAbbreviations = alternativeAbbreviations;
    }

}
