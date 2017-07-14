package org.gobiiproject.gobiiclient.core.common;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class encapsulates the retrieval of configuration values necessary for
 * the test framework. It is _the_ only class in gobii-client that is allowed to
 * instance the configuration directly. We require the rest of the framework
 * to get configuration data through the configuration web service: aside from
 * the test environment, we never ever want to create a direct dependency of
 * a client on a configuration file because we need configuration to remain
 * dynamic through central control in the server. While there are a few caes in
 * which clients of this class will access ConfigSettings, centralizing the the access
 * through this class enables us to easily trace the dependencies. It should be that the
 * only consumers of ConfigSettings in gobii-client are the tets -- not the client access
 * framework.
 *
 * Do use this mechanism, the fqpn of the configuraiton file must be defined as an environment
 * variable. In the JUnit configuration in the IDE, the "VM Options" would have:
 *                  -DcfgFqpn=C:\gobii-config\gobii-web.xml
 *
 * Something similar will be required on the mavn command line, like:
 *                  mvn test -DcfgFqpn=C:\gobii-config\gobii-web.xml
 */
public class TestConfiguration {

    private static Logger LOGGER = LoggerFactory.getLogger(TestConfiguration.class);

    private ConfigSettings configSettings = null;

    public ConfigSettings getConfigSettings() {
        return configSettings;
    }

    private static String CONFIG_FILE_LOCATION_PROP = "cfgFqpn";

    public TestConfiguration() throws Exception {

        String configFileLocation = System.getProperty(CONFIG_FILE_LOCATION_PROP);

        if (configFileLocation != null) {
            LOGGER.info("FQPN of configuration file read from environment variable " + CONFIG_FILE_LOCATION_PROP + ": " + configFileLocation);
        } else {
            String message = "The the environment does not define the FQPN of configuration in environment variable: " + CONFIG_FILE_LOCATION_PROP;
            LOGGER.error(message);
            throw new Exception(message);
        }

        ConfigSettings configSettings = new ConfigSettings(configFileLocation);
        this.configSettings = configSettings;
    }
}
