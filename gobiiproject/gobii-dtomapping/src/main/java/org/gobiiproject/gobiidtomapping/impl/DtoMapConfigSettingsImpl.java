package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidtomapping.DtoMapConfigSettings;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.dto.container.ConfigSettingsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 6/10/2016.
 */
public class DtoMapConfigSettingsImpl implements DtoMapConfigSettings {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapConfigSettingsImpl.class);


    @Override
    public ConfigSettingsDTO readSettings(ConfigSettingsDTO configSettingsDTO) {
        ConfigSettingsDTO returnVal = configSettingsDTO;

        try {
            ConfigSettings configSettings = new ConfigSettings();
            returnVal.setDefaultCrop(configSettings.getDefaultGobiiCropType());
            for (CropConfig currentCropConfig : configSettings.getActiveCropConfigs()) {

                ServerConfig currentServerConfig = new ServerConfig(currentCropConfig);

                returnVal.getServerConfigs().put(currentCropConfig.getGobiiCropType(),
                        currentServerConfig);
            }

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }


        return returnVal;
    }
}
