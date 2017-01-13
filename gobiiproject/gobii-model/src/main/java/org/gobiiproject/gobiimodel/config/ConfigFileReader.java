package org.gobiiproject.gobiimodel.config;

import org.apache.commons.lang.SystemUtils;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.springframework.jndi.JndiTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Phil on 4/12/2016.
 */
public class ConfigFileReader {

    private String fqpn = null;
    public ConfigFileReader(String fqpn) {

        this.fqpn = fqpn;

    }


    private Properties webProperties = null;

    private Properties getWebProperties() throws Exception {

        if (null == webProperties) {

            String configFileWebPath = null;
            if( null == this.fqpn ) {

                JndiTemplate jndi = new JndiTemplate();
                configFileWebPath = (String) jndi.lookup("java:comp/env/gobiipropsloc");
            } else {
                configFileWebPath = this.fqpn;
            }

            if (!LineUtils.isNullOrEmpty(configFileWebPath) ) {

                InputStream configFileWebStream = new FileInputStream(configFileWebPath);
                if (null != configFileWebStream) {

                    webProperties = new Properties();
                    webProperties.load(configFileWebStream);

                } else {
                    throw new Exception("Unable to create input stream for config file: " + configFileWebPath);
                }

            } else {
                throw new Exception("JNDI lookup on prop file location is null or empty" );
            }

        }

        return webProperties;

    } // getWebProperties()

    public String getPropValue(String propName) throws Exception {

        String returnVal = getWebProperties().getProperty(propName);
        if (null == returnVal) {
            returnVal = ""; //prevent NPEs
        }

        return returnVal;

    } // getPropValue()

} // ConfigFileReader
