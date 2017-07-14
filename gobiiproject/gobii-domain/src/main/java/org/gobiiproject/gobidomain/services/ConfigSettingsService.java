package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ConfigSettingsDTO;

/**
 * Created by Phil on 4/12/2016.
 */
public interface ConfigSettingsService {
    ConfigSettingsDTO getConfigSettings() throws GobiiException;
}
