package org.gobiiproject.gobiiapimodel.restresources.common;

/**
 * Created by Phil on 9/7/2016.
 */
public class ResourceParam {

    public enum ResourceParamType {Unknown, UriParam, QueryParam}

    public ResourceParam(ResourceParamType resourceParamType,
                         String name,
                         String value) {

        this.resourceParamType = resourceParamType;
        this.name = name;
        this.value = value;

    } // ctor

    private ResourceParamType resourceParamType = ResourceParamType.Unknown;
    private String name;
    private String value;
    boolean required = true;


    public ResourceParamType getResourceParamType() {
        return resourceParamType;
    }

    public String getName() {
        return name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
