package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiDbType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 5/5/2016.
 */
public class CropConfig {


    private GobiiCropType gobiiCropType;
    private String serviceDomain;
    private String serviceAppRoot;
    private Integer servicePort;
    private String rawUserFilesDirectory;
    private String loaderInstructionFilesDirectory;
    private String extractorInstructionFilesDirectory;
    private String extractorInstructionFilesOutputDirectory;
    private String intermediateFilesDirectory;
    private boolean isActive = false;
    private Map<GobiiDbType, CropDbConfig> dbConfigByDbType = new HashMap<>();

    public CropConfig(GobiiCropType gobiiCropType,
                      String serviceDomain,
                      String serviceAppRoot,
                      Integer servicePort,
                      String loaderInstructionFilesDirectory,
                      String extractorInstructionFilesDirectory,
                      String extractorInstructionFilesOutputDirectory,
                      String rawUserFilesDirectory,
                      String intermediateFilesDirectory,
                      boolean isActive) {

        this.gobiiCropType = gobiiCropType;
        this.serviceDomain = serviceDomain;
        this.serviceAppRoot = serviceAppRoot;
        this.servicePort = servicePort;
        this.rawUserFilesDirectory = rawUserFilesDirectory;
        this.loaderInstructionFilesDirectory = loaderInstructionFilesDirectory;
        this.extractorInstructionFilesDirectory = extractorInstructionFilesDirectory;
        this.extractorInstructionFilesOutputDirectory = extractorInstructionFilesOutputDirectory;
        this.intermediateFilesDirectory = intermediateFilesDirectory;
        this.isActive = isActive;
    }

    public Integer getServicePort() {
        return servicePort;
    }

    public String getRawUserFilesDirectory() {
        return rawUserFilesDirectory;
    }

    public String getLoaderInstructionFilesDirectory() {
        return loaderInstructionFilesDirectory;
    }

    public String getExtractorInstructionFilesDirectory() {
        return extractorInstructionFilesDirectory;
    }

    public String getExtractorInstructionFilesOutputDirectory() {
        return extractorInstructionFilesOutputDirectory;
    }

    public String getIntermediateFilesDirectory() {
        return intermediateFilesDirectory;
    }

    public String getServiceDomain() {
        return serviceDomain;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getServiceAppRoot() {
        return serviceAppRoot;
    }

    public void setServiceAppRoot(String serviceAppRoot) {
        this.serviceAppRoot = serviceAppRoot;
    }

    public GobiiCropType getGobiiCropType() {
        return gobiiCropType;
    }

    public void setGobiiCropType(GobiiCropType gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
    }

    public void addCropDbConfig(GobiiDbType gobiiDbTypee, CropDbConfig cropDbConfig) {
        dbConfigByDbType.put(gobiiDbTypee, cropDbConfig);
    } // addCropDbConfig()

    public CropDbConfig getCropDbConfig(GobiiDbType gobiiDbType) {
        return dbConfigByDbType.get(gobiiDbType);
    } // getCropDbConfig()


}
