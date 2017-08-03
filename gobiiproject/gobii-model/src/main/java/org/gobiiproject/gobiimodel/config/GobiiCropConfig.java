package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains the web server configuration properties necessary for a given
 * crop. In addition, it contains GobiiCropDbConfig instances for the databae configurations
 * for the specific crop.
 */
@Root
public class GobiiCropConfig extends ServerBase {


    @Element(required = false)
    private String gobiiCropType;

    @ElementMap(required = false)
    private Map<GobiiDbType, GobiiCropDbConfig> cropDbConfigsByDbType = new HashMap<>();

    public GobiiCropConfig() {
    }

    public GobiiCropConfig(String gobiiCropType,
                           String host,
                           String contextPath,
                           Integer port,
                           boolean isActive,
                           boolean decrypt) {

        super(host,contextPath,port,isActive);
        this.gobiiCropType = gobiiCropType;
    }

    public void setCropDbConfig(GobiiDbType gobiiDbType,
                                String host,
                                String dbName,
                                Integer port,
                                String userName,
                                String password) {

        GobiiCropDbConfig gobiiCropDbConfig = this.cropDbConfigsByDbType.get(gobiiDbType);
        if (gobiiCropDbConfig == null) {

            gobiiCropDbConfig = new GobiiCropDbConfig();
            this.cropDbConfigsByDbType.put(gobiiDbType, gobiiCropDbConfig);

        }

        gobiiCropDbConfig
                .setGobiiDbType(gobiiDbType)
                .setHost(host)
                .setContextPath(dbName)
                .setPort(port)
                .setUserName(userName)
                .setPassword(password);
    }

    public GobiiCropConfig setHost(String host) {
        super.setHost(host);
        return this;
    }

    public GobiiCropConfig setPort(Integer port) {
        super.setPort(port);
        return this;
    }

    public GobiiCropConfig setCropDbConfigsByDbType(Map<GobiiDbType, GobiiCropDbConfig> cropDbConfigsByDbType) {
        this.cropDbConfigsByDbType = cropDbConfigsByDbType;
        return this;
    }

    public GobiiCropConfig setActive(boolean active) {
        super.setActive(active);
        return this;
    }


    public GobiiCropConfig setContextPath(String contextPath) {
        super.setContextPath(contextPath);
        return this;
    }

    public String getGobiiCropType() {
        return gobiiCropType;
    }

    public GobiiCropConfig setGobiiCropType(String gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
        return this;
    }

    public void addCropDbConfig(GobiiDbType gobiiDbTypee, GobiiCropDbConfig gobiiCropDbConfig) {
        cropDbConfigsByDbType.put(gobiiDbTypee, gobiiCropDbConfig);

    } // addCropDbConfig()

    public GobiiCropDbConfig getCropDbConfig(GobiiDbType gobiiDbType) {
        GobiiCropDbConfig returnVal = this.cropDbConfigsByDbType.get(gobiiDbType);
        return returnVal;
    } // getCropDbConfig()

    public Collection<GobiiCropDbConfig> getCropConfigs() {
        return this.cropDbConfigsByDbType.values();
    }

}
