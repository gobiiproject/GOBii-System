package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiFileLocationType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 6/10/2016.
 */
public class ServerConfig {

    public ServerConfig() {
    }

    public ServerConfig(CropConfig cropConfig) {

        this.port = cropConfig.getServicePort();
        this.domain = cropConfig.getServiceDomain();
        this.contextRoot = cropConfig.getServiceAppRoot();
        this.gobiiCropType = cropConfig.getGobiiCropType();

        fileLocations
                .put(GobiiFileLocationType.EXTRACTORINSTRUCTION_FILES, cropConfig.getExtractorInstructionFilesDirectory());

        fileLocations
                .put(GobiiFileLocationType.LOADERINSTRUCTION_FILES, cropConfig.getLoaderInstructionFilesDirectory());

        fileLocations
                .put(GobiiFileLocationType.INTERMEDIATE_FILES, cropConfig.getIntermediateFilesDirectory());

        fileLocations
                .put(GobiiFileLocationType.RAWUSER_FILES, cropConfig.getRawUserFilesDirectory());

    }

    private Integer port;
    private String domain;
    private String contextRoot;
    private GobiiCropType gobiiCropType;
    private Map<GobiiFileLocationType, String> fileLocations = new HashMap<>();

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

    public GobiiCropType getGobiiCropType() {
        return gobiiCropType;
    }

    public void setGobiiCropType(GobiiCropType gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
    }

    public Map<GobiiFileLocationType, String> getFileLocations() {
        return fileLocations;
    }

    public void setFileLocations(Map<GobiiFileLocationType, String> fileLocations) {
        this.fileLocations = fileLocations;
    }
}
