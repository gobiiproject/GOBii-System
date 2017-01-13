package org.gobiiproject.gobiibrapi.calls.studies.observationvariables;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Phil on 12/18/2016.
 */
public class ObservationMethhod {

    public ObservationMethhod() {}

    public ObservationMethhod(String methodDbId, String name) {
        this.methodDbId = methodDbId;
        this.name = name;
    }

    private String methodDbId;

    private String description;

    private String name;

    private String methodClass;

    private String reference;

    private String formula;

    public String getMethodDbId ()
    {
        return methodDbId;
    }

    public void setMethodDbId (String methodDbId)
    {
        this.methodDbId = methodDbId;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @JsonProperty("class")
    public String getMethodClass ()
    {
        return methodClass;
    }

    @JsonProperty("class")
    public void setMethodClass (String methodClass)
    {
        this.methodClass = methodClass;
    }

    public String getReference ()
    {
        return reference;
    }

    public void setReference (String reference)
    {
        this.reference = reference;
    }

    public String getFormula ()
    {
        return formula;
    }

    public void setFormula (String formula)
    {
        this.formula = formula;
    }

}
