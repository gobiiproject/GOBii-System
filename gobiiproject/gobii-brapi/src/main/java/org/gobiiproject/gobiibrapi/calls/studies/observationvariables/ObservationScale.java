package org.gobiiproject.gobiibrapi.calls.studies.observationvariables;

/**
 * Created by Phil on 12/18/2016.
 */
public class ObservationScale {

    public ObservationScale() {}

    public ObservationScale(String scaleDbId, String name) {
        this.scaleDbId = scaleDbId;
        this.name = name;
    }

    private ScaleValidValues scaleValidValues;

    private String scaleDbId;

    private String name;

    private String xref;

    private String datatype;

    private String decimalPlaces;

    public ScaleValidValues getScaleValidValues()
    {
        return scaleValidValues;
    }

    public void setScaleValidValues(ScaleValidValues scaleValidValues)
    {
        this.scaleValidValues = scaleValidValues;
    }

    public String getScaleDbId ()
    {
        return scaleDbId;
    }

    public void setScaleDbId (String scaleDbId)
    {
        this.scaleDbId = scaleDbId;
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

    public String getDatatype ()
    {
        return datatype;
    }

    public void setDatatype (String datatype)
    {
        this.datatype = datatype;
    }

    public String getDecimalPlaces ()
    {
        return decimalPlaces;
    }

    public void setDecimalPlaces (String decimalPlaces)
    {
        this.decimalPlaces = decimalPlaces;
    }

}
