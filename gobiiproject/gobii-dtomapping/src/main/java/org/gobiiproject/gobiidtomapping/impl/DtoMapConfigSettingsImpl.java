package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidtomapping.DtoMapConfigSettings;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.headerlesscontainer.ConfigSettingsDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.ServerCapabilityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 6/10/2016.
 */
public class DtoMapConfigSettingsImpl implements DtoMapConfigSettings {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapConfigSettingsImpl.class);


    @Override
    public ConfigSettingsDTO readSettings() throws GobiiException{

        ConfigSettingsDTO returnVal = new ConfigSettingsDTO();

        try {
            ConfigSettings configSettings = new ConfigSettings();

             returnVal.setServerCapabilities(configSettings.getServerCapabilities());

            for (GobiiCropConfig currentGobiiCropConfig : configSettings.getActiveCropConfigs()) {

                ServerConfig currentServerConfig = new ServerConfig(currentGobiiCropConfig,
                        configSettings.getProcessingPath(currentGobiiCropConfig.getGobiiCropType(),
                                GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS),
                        configSettings.getProcessingPath(currentGobiiCropConfig.getGobiiCropType(),
                                GobiiFileProcessDir.LOADER_INSTRUCTIONS),
                        configSettings.getProcessingPath(currentGobiiCropConfig.getGobiiCropType(),
                                GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES),
                        configSettings.getProcessingPath(currentGobiiCropConfig.getGobiiCropType(),
                                GobiiFileProcessDir.RAW_USER_FILES)
                        );

                returnVal.getServerConfigs().put(currentGobiiCropConfig.getGobiiCropType(),
                        currentServerConfig);
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }


        return returnVal;
    }
}
