package org.gobiiproject.gobiibrapi.calls.studies.observationvariables;

import java.util.List;

/**
 * Created by Phil on 12/18/2016.
 */
public class ScaleValidValues {


    public ScaleValidValues() {}

    public ScaleValidValues(String min, String max, List<String> categories) {
        this.min = min;
        this.max = max;
        this.categories = categories;
    }

    private String min;

    private String max;

    private List<String> categories;

    public String getMin ()
    {
        return min;
    }

    public void setMin (String min)
    {
        this.min = min;
    }

    public String getMax ()
    {
        return max;
    }

    public void setMax (String max)
    {
        this.max = max;
    }

    public List<String> getCategories ()
    {
        return categories;
    }

    public void setCategories (List<String> categories)
    {
        this.categories = categories;
    }

}
