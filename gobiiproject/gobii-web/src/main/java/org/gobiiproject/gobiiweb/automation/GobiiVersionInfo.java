package org.gobiiproject.gobiiweb.automation;

import java.util.Properties;

/**
 * Created by VCalaminos on 2/10/2017.
 */
public class GobiiVersionInfo {

    private String version;

    public GobiiVersionInfo() throws Exception {

        Properties properties = new Properties();

        properties.load(this.getClass().getClassLoader().getResourceAsStream("gobii_web_props.properties"));

        this.version = properties.getProperty("gobii_version").toString();

    }

    public static String getVersion() throws Exception {

        GobiiVersionInfo gobiiVersionInfo = new GobiiVersionInfo();

        return gobiiVersionInfo.version;
    }

}
