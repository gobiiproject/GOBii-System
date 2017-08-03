package org.gobiiproject.gobiiprocess.web;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;

/**
 * Simple process class for example of reading configuration files.
 * Created by Phil on 6/24/2016.
 */
public class PlainLoaderEmulator {

    public static void main(String[] args) {

        try {

            ConfigSettings configSettings = new ConfigSettings("C:\\gobii-config\\gobii-web.properties");
            for(GobiiCropConfig currentGobiiCropConfig : configSettings.getActiveCropConfigs() ) {
                System.out.println(currentGobiiCropConfig.getHost());
            }


        } catch (Exception e ) {
            e.printStackTrace();
        }
    } // main()
}
