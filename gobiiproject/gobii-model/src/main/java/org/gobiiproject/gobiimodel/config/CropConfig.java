package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class contains the web server configuration properties necessary for a given
 * crop. In addition, it contains CropDbConfig instances for the databae configurations
 * for the specific crop.
 */
@Root
public class CropConfig {


    @Element(required = false)
    private String gobiiCropType;

    @Element(required = false)
    private String serviceDomain;

    @Element(required = false)
    private String serviceAppRoot;

    @Element(required = false)
    private Integer servicePort;

    @Element(required = false)
    private boolean isActive;

    @ElementMap(required = false)
    private Map<GobiiDbType, CropDbConfig> cropDbConfigsByDbType = new HashMap<>();

    public CropConfig() {
    }

    public CropConfig(String gobiiCropType,
                      String serviceDomain,
                      String serviceAppRoot,
                      Integer servicePort,
                      boolean isActive,
                      boolean decrypt) {

        this.gobiiCropType = gobiiCropType;
        this.serviceDomain = serviceDomain;
        this.serviceAppRoot = serviceAppRoot;
        this.servicePort = servicePort;
        this.isActive = isActive;

    }

    public void setCropDbConfig(GobiiDbType gobiiDbType,
                                String host,
                                String dbName,
                                Integer port,
                                String userName,
                                String password) {

        CropDbConfig cropDbConfig = this.cropDbConfigsByDbType.get(gobiiDbType);
        if (cropDbConfig == null) {

            cropDbConfig = new CropDbConfig();
            this.cropDbConfigsByDbType.put(gobiiDbType, cropDbConfig);

        }

        cropDbConfig
                .setGobiiDbType(gobiiDbType)
                .setHost(host)
                .setDbName(dbName)
                .setPort(port)
                .setUserName(userName)
                .setPassword(password);
    }

    public CropConfig setServiceDomain(String serviceDomain) {
        this.serviceDomain = serviceDomain;
        return this;
    }

    public CropConfig setServicePort(Integer servicePort) {
        this.servicePort = servicePort;
        return this;
    }

    public CropConfig setCropDbConfigsByDbType(Map<GobiiDbType, CropDbConfig> cropDbConfigsByDbType) {
        this.cropDbConfigsByDbType = cropDbConfigsByDbType;
        return this;
    }

    public Integer getServicePort() {
        return servicePort;
    }


    public String getServiceDomain() {

        return serviceDomain;
    }

    public boolean isActive() {
        return isActive;
    }

    public CropConfig setActive(boolean active) {
        isActive = active;
        return this;
    }

    public String getServiceAppRoot() {

        return LineUtils.terminateDirectoryPath(this.serviceAppRoot);
    }

    public CropConfig setServiceAppRoot(String serviceAppRoot) {
        this.serviceAppRoot = serviceAppRoot;
        return this;
    }

    public String getGobiiCropType() {
        return gobiiCropType;
    }

    public CropConfig setGobiiCropType(String gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
        return this;
    }

    public void addCropDbConfig(GobiiDbType gobiiDbTypee, CropDbConfig cropDbConfig) {
        cropDbConfigsByDbType.put(gobiiDbTypee, cropDbConfig);

    } // addCropDbConfig()

    public CropDbConfig getCropDbConfig(GobiiDbType gobiiDbType) {
        CropDbConfig returnVal = this.cropDbConfigsByDbType.get(gobiiDbType);
        return returnVal;
    } // getCropDbConfig()

    public Collection<CropDbConfig> getCropConfigs() {
        return this.cropDbConfigsByDbType.values();
    }

}
