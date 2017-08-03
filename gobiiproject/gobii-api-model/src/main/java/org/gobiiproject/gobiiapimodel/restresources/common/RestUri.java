package org.gobiiproject.gobiiapimodel.restresources.common;

import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.gobiiproject.gobiimodel.utils.LineUtils;


import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Phil on 9/7/2016.
 */
public class RestUri {

    public static char URL_SEPARATOR = '/';
    private final String DELIM_PARAM_BEGIN = "{";
    private final String DELIM_PARAM_END = "}";

    private String domain = null;
    private Integer port = null;
    private String contextPath;
    private String contextRoot;

    private String requestTemplate;
    private Map<String, ResourceParam> paramMap = new HashMap<>();
    private List<ResourceParam> resourceParams = new ArrayList<>();

    private Map<String, String> httpHeaders = new HashMap<>();
    private String destinationFilenPath = null;


    public RestUri(String domain, Integer port, String contextRoot, String contextPath, String resourcePath) throws Exception {
        this(contextRoot,contextPath,resourcePath);
        this.domain = domain;
        this.port = port;
    }

    public RestUri(String contextRoot, String contextPath, String resourcePath) throws Exception {
        this.contextRoot = this.delimitSegment(contextRoot);
        this.contextPath = contextPath;
        this.requestTemplate = this.contextRoot + this.contextPath + resourcePath;

        // set default content type; this can be overridden by withHeaders()
        this.httpHeaders.put(GobiiHttpHeaderNames.HEADER_NAME_CONTENT_TYPE,
                MediaType.APPLICATION_JSON);

        this.httpHeaders.put(GobiiHttpHeaderNames.HEADER_NAME_ACCEPT,
                MediaType.APPLICATION_JSON);

    }

    public RestUri(String restUri) {
        this.requestTemplate = restUri;
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public String getResource() throws Exception {


        StringBuilder stringBuilder = new StringBuilder(this.getResourcePath());

        if (stringBuilder.charAt(0) == URL_SEPARATOR) {
            stringBuilder.deleteCharAt(0);
        }

        Integer ctxRootIdx = stringBuilder.indexOf(this.contextRoot);
        if (ctxRootIdx >= 0) {
            stringBuilder.delete(ctxRootIdx, this.contextRoot.length());
        }

        Integer ctxPathIdx = stringBuilder.indexOf(this.contextPath);
        if (ctxPathIdx >= 0) {
            stringBuilder.delete(ctxPathIdx, this.contextPath.length());
        }

        String returnVal = stringBuilder.toString();

        returnVal = stringBuilder.toString();
        return returnVal;

    }

    public String getResourcePath() throws Exception {

        String returnVal;

        Integer idxOfLastDelimiter = this.requestTemplate.length() - 1;
        if (this.requestTemplate.charAt(idxOfLastDelimiter) == URL_SEPARATOR) {
            returnVal = requestTemplate.substring(0, idxOfLastDelimiter);
        } else {
            returnVal = this.requestTemplate;
        }

        return RestUri.URL_SEPARATOR
                + returnVal
                .replace(this.contextPath, "")
                .replace(this.contextRoot, "");
    }

    private String delimitSegment(String segment) {

        String returnVal = segment;

        if (null != returnVal) {
            if (returnVal.lastIndexOf(RestUri.URL_SEPARATOR) != returnVal.length() - 1) {
                returnVal = returnVal + RestUri.URL_SEPARATOR;
            }
        }

        return returnVal;
    }


    public List<ResourceParam> getRequestParams() {
        return this.resourceParams
                .stream()
                .filter(getParam -> getParam.getResourceParamType().equals(ResourceParam.ResourceParamType.QueryParam))
                .collect(Collectors.toList());
    }

    public RestUri addQueryParam(String name) {
        this.addParam(ResourceParam.ResourceParamType.QueryParam, name);
        return this;
    }

    public RestUri addQueryParam(String name, String value) throws Exception {
        this.addQueryParam(name);
        this.setParamValue(name, value);
        return this;
    }


    public RestUri addUriParam(String name) {
        this.addParam(ResourceParam.ResourceParamType.UriParam, name);
        return this;
    }

    public RestUri addUriParam(String name, String value) throws Exception {
        this.addUriParam(name);
        this.setParamValue(name, value);
        return this;
    }

    private RestUri addParam(ResourceParam.ResourceParamType resourceParamType,
                             String name) {

        if (resourceParamType.equals(ResourceParam.ResourceParamType.UriParam)) {
            this.appendPathVariable(name);
        }

        ResourceParam resourceParam = new ResourceParam(resourceParamType, name, null);
        this.paramMap.put(resourceParam.getName(), resourceParam);
        this.resourceParams.add(resourceParam);

        return this;

    }

    public RestUri setParamValue(String paramName, String value) throws Exception {

        if (null == this.paramMap.get(paramName)) {
            throw new Exception("Specified parameter does not exist: " + paramName);
        }

        this.paramMap.get(paramName).setValue(value);

        return this;
    }

    public RestUri appendSegment(GobiiServiceRequestId gobiiServiceRequestId) throws Exception {

        this.requestTemplate = this.delimitSegment(this.requestTemplate);
        String segment = gobiiServiceRequestId.getResourcePath();

        this.requestTemplate += this.delimitSegment(segment);

        return this;
    }

    public RestUri appendPathVariable(String paramName) {

        this.requestTemplate = this.delimitSegment(this.requestTemplate);

        this.requestTemplate += this.DELIM_PARAM_BEGIN + paramName + this.DELIM_PARAM_END;

        return this;

    }

    public RestUri withHttpHeader(String headerName, String headerValue) {
        this.httpHeaders.put(headerName, headerValue);
        return this;
    }


    public RestUri withDestinationFqpn(String destinationPath) {
        this.destinationFilenPath = destinationPath;
        return this;
    }

    public String getDestinationFqpn() {
        return this.destinationFilenPath;
    }

    public String makeUrlComplete() throws Exception {

        if(LineUtils.isNullOrEmpty(this.domain) || this.port == null ) {
            throw new Exception("Domain and Port values are required");
        }

        String returnVal = this.domain + ":" + this.port + "/" + this.makeUrlWithQueryParams();

        returnVal = returnVal.replace("//", "/");

        returnVal = "http://" + returnVal;

        return returnVal;

    }


    /***
     * Makes a url with current uri parameters and adds current query parameters.
     * We don't add the query parameters by default because the components in
     * HttpCore want to to add these params through its API. So in the nominal case
     * we don't add the query params.
     * @return
     * @throws Exception
     */
    public String makeUrlWithQueryParams() throws Exception {

        String returnVal = this.makeUrlPath();
        returnVal += "?";
        for (ResourceParam currentParam : this.getRequestParams()) {
            returnVal += currentParam.getName() + "=" + currentParam.getValue();
        }

        return returnVal;
    }

    public String makeUrlPath() throws Exception {

        String returnVal = this.requestTemplate; // in case there are no path variables

        List<ResourceParam> uriParams = resourceParams
                .stream()
                .filter(getRequestParam -> getRequestParam.getResourceParamType()
                        .equals(ResourceParam.ResourceParamType.UriParam))
                .collect(Collectors.toList());

        for (ResourceParam currentParam : uriParams) {
            String paramToReplace = this.DELIM_PARAM_BEGIN
                    + currentParam.getName()
                    + this.DELIM_PARAM_END;

            if (this.requestTemplate.contains(paramToReplace)) {

                if (false == LineUtils.isNullOrEmpty(currentParam.getValue())) {

                    returnVal = returnVal.replace(paramToReplace, currentParam.getValue());
                } else {
                    throw new Exception("The path variable parameter "
                            + paramToReplace
                            + " does not have a value");
                }
            } else {
                throw new Exception("The request template "
                        + this.requestTemplate
                        + " does not contain the path path variable "
                        + paramToReplace);
            }
        }

        if (returnVal.contains(this.DELIM_PARAM_BEGIN)) {
            String missingParameter = returnVal
                    .substring(
                            returnVal.indexOf(this.DELIM_PARAM_BEGIN),
                            returnVal.indexOf(this.DELIM_PARAM_END)
                    );

            throw new Exception("There is no parameter for uri parameter " + missingParameter);
        }

        return returnVal;

    } // makeUrlPath

} // class RestUri
