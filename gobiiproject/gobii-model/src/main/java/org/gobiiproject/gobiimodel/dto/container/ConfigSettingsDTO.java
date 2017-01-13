package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.types.GobiiCropType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/8/2016.
 */
public class ConfigSettingsDTO extends DtoMetaData {


    private Map<GobiiCropType, ServerConfig> serverConfigs = new HashMap<>();

    public Map<GobiiCropType, ServerConfig> getServerConfigs() {
        return serverConfigs;
    }

    public void setServerConfigs(Map<GobiiCropType, ServerConfig> serverConfigs) {
        this.serverConfigs = serverConfigs;
    }

    GobiiCropType defaultCrop = GobiiCropType.TEST;

    public GobiiCropType getDefaultCrop() {
        return defaultCrop;
    }

    public void setDefaultCrop(GobiiCropType defaultCrop) {
        this.defaultCrop = defaultCrop;
    }
}
