package org.gobiiproject.gobiimodel.config;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.Map;


@Root
public class ServerConfigKDC extends ServerBase {

    public enum KDCResource {
        QC_START,
        QC_STATUS_,
        QC_DOWNLOAD,
        QC_PURGE
    }

    @ElementMap(required = false)
    Map<KDCResource, String> kdcResources = new HashMap<>();

    @Element(required = false)
    Integer statusCheckIntervalSecs = 0;

    @Element(required = false)
    Integer maxStatusCheckMins = 0;

    public ServerConfigKDC() {
    }

    public ServerConfigKDC(String host,
                           String contextPath,
                           Integer port,
                           boolean isActive,
                           boolean decrypt) {

        super(host, contextPath, port, isActive);
    }




    public ServerConfigKDC addPath(KDCResource kdcResource, String resource) {
        this.kdcResources.put(kdcResource, resource);
        return this;
    }

    public String getPath(KDCResource kdcResource) {

        String returnVal = null;

        if (this.kdcResources.containsKey(kdcResource)) {
            returnVal = this.kdcResources.get(kdcResource);
        }

        return returnVal;
    }

    public ServerConfigKDC setHost(String host) {
        super.setHost(host);
        return this;
    }

    public ServerConfigKDC setPort(Integer port) {
        super.setPort(port);
        return this;
    }

    public ServerConfigKDC setActive(boolean active) {
        super.setActive(active);
        return this;
    }

    public ServerConfigKDC setContextPath(String contextPath) {
        super.setContextPath(contextPath);
        return this;
    }

    public Integer getStatusCheckIntervalSecs() {
        return statusCheckIntervalSecs;
    }

    public ServerConfigKDC setStatusCheckIntervalSecs(Integer statusCheckIntervalSecs) {
        this.statusCheckIntervalSecs = statusCheckIntervalSecs;
        return this;
    }

    public Integer getMaxStatusCheckMins() {
        return maxStatusCheckMins;
    }

    public ServerConfigKDC setMaxStatusCheckMins(Integer maxStatusCheckMins) {
        this.maxStatusCheckMins = maxStatusCheckMins;
        return this;
    }
}
