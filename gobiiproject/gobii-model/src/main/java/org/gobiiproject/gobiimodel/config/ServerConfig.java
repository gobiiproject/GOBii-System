package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.simpleframework.xml.Element;

import java.util.HashMap;
import java.util.Map;

/**
 This class encapsualtes the configuration properties that are returned to web clients via
 web services. For most intents and purposes, it is superfluous. However, for security purposes
 we use it to prevent sensitive configuration data from being sent through web services: it forces
 careful decisions to be made by the author of the web service that returns this data to the web
 client.
 */
public class ServerConfig {

    public ServerConfig() {
    }

    public ServerConfig(GobiiCropConfig gobiiCropConfig,
                        String extractorInstructionsDir,
                        String loaderInstructionsDir,
                        String intermediateFilesDir,
                        String rawUserFilesDir) {

        this.port = gobiiCropConfig.getPort();
        this.domain = gobiiCropConfig.getHost();
        this.contextRoot = gobiiCropConfig.getContextPath();
        this.gobiiCropType = gobiiCropConfig.getGobiiCropType();

        fileLocations
                .put(GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS, extractorInstructionsDir);

        fileLocations
                .put(GobiiFileProcessDir.LOADER_INSTRUCTIONS, loaderInstructionsDir);

        fileLocations
                .put(GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES, intermediateFilesDir);

        fileLocations
                .put(GobiiFileProcessDir.RAW_USER_FILES, rawUserFilesDir);

    }

    @Element(required = false)
    private Integer port = 0;

    @Element(required = false)
    private String domain = "";

    @Element(required = false)
    private String contextRoot = "";

    @Element(required = false)
    private String gobiiCropType;

    @Element
    private Map<GobiiFileProcessDir, String> fileLocations = new HashMap<>();

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getContextRoot() {
        return contextRoot;
    }

    public void setContextRoot(String contextRoot) {
        this.contextRoot = contextRoot;
    }

    public String getGobiiCropType() {
        return gobiiCropType;
    }

    public void setGobiiCropType(String gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
    }

    public Map<GobiiFileProcessDir, String> getFileLocations() {
        return fileLocations;
    }

    public void setFileLocations(Map<GobiiFileProcessDir, String> fileLocations) {
        this.fileLocations = fileLocations;
    }
}
