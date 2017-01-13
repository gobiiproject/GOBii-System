package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.ConfigSettingsService;
import org.gobiiproject.gobiidtomapping.DtoMapConfigSettings;
import org.gobiiproject.gobiimodel.dto.container.ConfigSettingsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Phil on 6/10/2016.
 */

public class ConfigSettingsServiceImpl implements ConfigSettingsService {

    Logger LOGGER = LoggerFactory.getLogger(ConfigSettingsServiceImpl.class);



    @Autowired
    DtoMapConfigSettings dtoMapConfigSettings;

    @Override
    public ConfigSettingsDTO process(ConfigSettingsDTO configSettingsDTO) {

        ConfigSettingsDTO returnVal = configSettingsDTO;

        try {

            returnVal = dtoMapConfigSettings.readSettings(returnVal);

        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii service error", e);
        }

        return returnVal;
    }
}
