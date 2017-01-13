package org.gobiiproject.gobiiprocess;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;

import java.io.Console;

/**
 * Created by Phil on 6/24/2016.
 */
public class PlainLoaderEmulator {

    public static void main(String[] args) {

        try {

            ConfigSettings configSettings = new ConfigSettings("C:\\gobii-config\\gobii-web.properties");
            for(CropConfig currentCropConfig : configSettings.getActiveCropConfigs() ) {
                System.out.println(currentCropConfig.getServiceDomain());
            }


        } catch (Exception e ) {
            e.printStackTrace();
        }
    } // main()
}
