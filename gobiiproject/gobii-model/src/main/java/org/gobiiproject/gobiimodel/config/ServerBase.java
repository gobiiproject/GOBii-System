package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ServerBase {


    @Element(required = false)
    private String host = "";

    @Element(required = false)
    private String contextPath = "";

    @Element(required = false)
    private Integer port = 0;

    @Element(required = false)
    private boolean isActive = false;

    public ServerBase() {
    }

    public ServerBase(String host,
                      String contextPath,
                      Integer port,
                      boolean isActive) {

        this.host = host;
        this.contextPath = contextPath;
        this.port = port;
        this.isActive = isActive;

    }


    public ServerBase setHost(String host) {
        this.host = host;
        return this;
    }

    public ServerBase setPort(Integer port) {
        this.port = port;
        return this;
    }

   public Integer getPort() {
        return port;
    }


    public String getHost() {

        return host;
    }

    public boolean isActive() {
        return isActive;
    }

    public ServerBase setActive(boolean active) {
        isActive = active;
        return this;
    }

    public String getContextPath() {
        return this.getContextPath(true);
    }

    public String getContextPath(boolean terminate) {

        String returnVal = this.contextPath;

        if( terminate && ! LineUtils.isNullOrEmpty(returnVal)) {
            returnVal = LineUtils.terminateDirectoryPath(returnVal);
        }
        return returnVal;
    }

    public ServerBase setContextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }


}
